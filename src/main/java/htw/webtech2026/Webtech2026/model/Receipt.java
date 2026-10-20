package htw.webtech2026.Webtech2026.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

// Regeln laut docs/use-cases.md, UC-04 und UC-05. Sie greifen, wenn ein Controller
// den Beleg mit @Valid entgegennimmt. Vom System gesetzte Felder haben keine Eingaberegeln.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private UUID id;

    @Size(max = 30, message = "Die Belegnummer darf höchstens 30 Zeichen lang sein.")
    private String number;

    @NotNull(message = "Die Belegart fehlt.")
    private ReceiptType type;

    @NotNull(message = "Das Ausstellungsdatum fehlt.")
    private LocalDate issueDate;

    @NotNull(message = "Das Datum der Zahlung fehlt.")
    @PastOrPresent(message = "Das Datum der Zahlung darf nicht in der Zukunft liegen.")
    private LocalDate paymentDate;

    @NotBlank(message = "Der Zahlungsempfänger fehlt.")
    @Size(max = 200, message = "Der Zahlungsempfänger darf höchstens 200 Zeichen lang sein.")
    private String payeeName;

    @Size(max = 500, message = "Die Anschrift darf höchstens 500 Zeichen lang sein.")
    private String payeeAddress;

    @NotBlank(message = "Die Art der Aufwendung fehlt.")
    @Size(max = 500, message = "Die Art der Aufwendung darf höchstens 500 Zeichen lang sein.")
    private String description;

    @NotNull(message = "Der Betrag fehlt.")
    @Positive(message = "Der Betrag muss größer als 0 sein.")
    private Long amountCents;

    @NotNull(message = "Die Umsatzsteuer-Auswahl fehlt.")
    private VatRate vatRate;

    @NotBlank(message = "Der Grund für den fehlenden Originalbeleg fehlt.")
    @Size(max = 500, message = "Der Grund darf höchstens 500 Zeichen lang sein.")
    private String reason;

    @NotNull(message = "Die Zahlungsart fehlt.")
    private PaymentMethod paymentMethod;

    // Kopie der Firmendaten beim Speichern (UC-03, UC-04); bleibt in M4 ohne Firmendaten null
    @Size(max = 200, message = "Der Name des Ausstellers darf höchstens 200 Zeichen lang sein.")
    private String issuerName;

    @Size(max = 500, message = "Die Anschrift des Ausstellers darf höchstens 500 Zeichen lang sein.")
    private String issuerAddress;

    private Instant createdAt;
    private UUID ownerId;   // bleibt bis zum Login null

    @JsonIgnore
    @AssertTrue(message = "Das Ausstellungsdatum darf nicht vor dem Datum der Zahlung liegen.")
    public boolean isIssueDateNotBeforePaymentDate() {
        return issueDate == null || paymentDate == null || !issueDate.isBefore(paymentDate);
    }

    @JsonIgnore
    @AssertTrue(message = "Bei einem Ankaufbeleg ist die Anschrift des Verkäufers Pflicht.")
    public boolean isPayeeAddressPresentForPurchase() {
        return type != ReceiptType.ANKAUF || (payeeAddress != null && !payeeAddress.isBlank());
    }
}
