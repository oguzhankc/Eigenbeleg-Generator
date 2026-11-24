package htw.webtech2026.Webtech2026.web;

import htw.webtech2026.Webtech2026.TestcontainersConfiguration;
import htw.webtech2026.Webtech2026.model.Receipt;
import htw.webtech2026.Webtech2026.model.ReceiptRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Läuft gegen eine echte PostgreSQL in Docker (TestcontainersConfiguration).
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ReceiptControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ReceiptRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
    }

    private static String validJson() {
        return """
                {
                  "type": "ALLGEMEIN",
                  "issueDate": "%s",
                  "paymentDate": "%s",
                  "payeeName": "Parkhaus Mitte GmbH",
                  "payeeAddress": "Musterstraße 1, 10115 Berlin",
                  "description": "Parkgebühr Kundentermin",
                  "amountCents": 450,
                  "vatRate": "REGELSATZ_19",
                  "reason": "Parkautomat gab keine Quittung aus",
                  "paymentMethod": "BAR"
                }
                """.formatted(LocalDate.now(), LocalDate.now().minusDays(1));
    }

    private String postReceipt(String json) throws Exception {
        return mockMvc.perform(post("/api/receipts").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void postSavesReceiptInDatabaseAndSetsSystemFields() throws Exception {
        mockMvc.perform(post("/api/receipts").contentType(MediaType.APPLICATION_JSON).content(validJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.number").value(matchesPattern("EB-" + Year.now() + "-\\d{4}")))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.amountCents").value(450))
                .andExpect(jsonPath("$.ownerId").value(nullValue()))
                .andExpect(jsonPath("$.issuerName").value(nullValue()))
                .andExpect(jsonPath("$.issueDateNotBeforePaymentDate").doesNotExist());

        assertThat(repository.findAll()).singleElement()
                .extracting(Receipt::getPayeeName).isEqualTo("Parkhaus Mitte GmbH");
    }

    @Test
    void ignoresSystemFieldsSentByClient() throws Exception {
        UUID foreignId = UUID.randomUUID();
        String json = validJson().replace("{", """
                {"id": "%s", "number": "FREMD-1", "ownerId": "%s", "issuerName": "Fremd GmbH",""".formatted(foreignId, foreignId));

        postReceipt(json);

        Receipt saved = repository.findAll().getFirst();
        assertThat(saved.getId()).isNotEqualTo(foreignId);
        assertThat(saved.getNumber()).startsWith("EB-");
        assertThat(saved.getOwnerId()).isNull();
        assertThat(saved.getIssuerName()).isNull();
    }

    @Test
    void numbersAreConsecutive() throws Exception {
        String first = JsonPath.read(postReceipt(validJson()), "$.number");
        String second = JsonPath.read(postReceipt(validJson()), "$.number");

        int firstCounter = Integer.parseInt(first.substring(first.lastIndexOf('-') + 1));
        assertThat(second).isEqualTo("EB-%d-%04d".formatted(Year.now().getValue(), firstCounter + 1));
    }

    @Test
    void invalidReceiptIsRejectedWith400AndFieldMessagesAndNotSaved() throws Exception {
        String json = validJson()
                .replace("\"amountCents\": 450", "\"amountCents\": 0")
                .replace("\"payeeName\": \"Parkhaus Mitte GmbH\"", "\"payeeName\": \"\"");

        mockMvc.perform(post("/api/receipts").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amountCents").value("Der Betrag muss größer als 0 sein."))
                .andExpect(jsonPath("$.payeeName").value("Der Zahlungsempfänger fehlt."));

        assertThat(repository.count()).isZero();
    }

    @Test
    void crossFieldRulesAreReportedAtTheirField() throws Exception {
        String json = validJson()
                .replace("\"type\": \"ALLGEMEIN\"", "\"type\": \"ANKAUF\"")
                .replace("\"payeeAddress\": \"Musterstraße 1, 10115 Berlin\"", "\"payeeAddress\": null")
                .replace("\"issueDate\": \"" + LocalDate.now(), "\"issueDate\": \"" + LocalDate.now().minusDays(5));

        mockMvc.perform(post("/api/receipts").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.payeeAddress").value("Bei einem Ankaufbeleg ist die Anschrift des Verkäufers Pflicht."))
                .andExpect(jsonPath("$.issueDate").value("Das Ausstellungsdatum darf nicht vor dem Datum der Zahlung liegen."));
    }

    @Test
    void getReadsFromDatabaseNewestFirst() throws Exception {
        mockMvc.perform(get("/api/receipts")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));

        String older = JsonPath.read(postReceipt(validJson()), "$.number");
        String newer = JsonPath.read(postReceipt(validJson()), "$.number");

        mockMvc.perform(get("/api/receipts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].number").value(newer))
                .andExpect(jsonPath("$[1].number").value(older));
    }
}
