package htw.webtech2026.Webtech2026.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReceiptControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ReceiptController()).build();

    @Test
    void returnsSampleReceiptsAsJson() throws Exception {
        mockMvc.perform(get("/api/receipts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].number").value("EB-2026-0001"))
                .andExpect(jsonPath("$[0].issueDate").value("2026-10-01"))
                .andExpect(jsonPath("$[1].type").value("ANKAUF"))
                .andExpect(jsonPath("$[1].amountCents").value(18000));
    }

    @Test
    void containsIssuerFieldsAndHidesValidationHelpers() throws Exception {
        mockMvc.perform(get("/api/receipts"))
                .andExpect(jsonPath("$[0].issuerName").value(nullValue()))
                .andExpect(jsonPath("$[0].issuerAddress").value(nullValue()))
                .andExpect(jsonPath("$[0].issueDateNotBeforePaymentDate").doesNotExist())
                .andExpect(jsonPath("$[0].payeeAddressPresentForPurchase").doesNotExist());
    }
}
