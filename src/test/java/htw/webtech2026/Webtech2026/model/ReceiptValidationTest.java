package htw.webtech2026.Webtech2026.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptValidationTest {

    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Receipt.ReceiptBuilder validReceipt() {
        return Receipt.builder()
                .type(ReceiptType.ALLGEMEIN)
                .issueDate(LocalDate.now())
                .paymentDate(LocalDate.now().minusDays(1))
                .payeeName("Parkhaus Mitte GmbH")
                .description("Parkgebühr Kundentermin")
                .amountCents(450L)
                .vatRate(VatRate.REGELSATZ_19)
                .reason("Parkautomat gab keine Quittung aus")
                .paymentMethod(PaymentMethod.BAR);
    }

    private static Set<String> violatedFields(Receipt receipt) {
        return validator.validate(receipt).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }

    @Test
    void validReceiptHasNoViolations() {
        assertThat(violatedFields(validReceipt().build())).isEmpty();
    }

    @Test
    void emptyReceiptReportsEveryRequiredFieldButNoSystemField() {
        Set<String> violated = violatedFields(Receipt.builder().build());

        assertThat(violated).containsExactlyInAnyOrder(
                "type", "issueDate", "paymentDate", "payeeName", "description",
                "amountCents", "vatRate", "reason", "paymentMethod");
    }

    @Test
    void amountMustBeGreaterThanZero() {
        assertThat(violatedFields(validReceipt().amountCents(0L).build())).containsExactly("amountCents");
    }

    @Test
    void paymentDateMustNotBeInTheFuture() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        assertThat(violatedFields(validReceipt().paymentDate(tomorrow).issueDate(tomorrow).build()))
                .containsExactly("paymentDate");
    }

    @Test
    void issueDateMustNotBeBeforePaymentDate() {
        Receipt receipt = validReceipt()
                .paymentDate(LocalDate.now())
                .issueDate(LocalDate.now().minusDays(1))
                .build();

        assertThat(violatedFields(receipt)).containsExactly("issueDateNotBeforePaymentDate");
    }

    @Test
    void purchaseReceiptRequiresSellerAddress() {
        Receipt.ReceiptBuilder purchase = validReceipt().type(ReceiptType.ANKAUF).vatRate(VatRate.OHNE_UST);

        assertThat(violatedFields(purchase.payeeAddress(null).build())).containsExactly("payeeAddressPresentForPurchase");
        assertThat(violatedFields(purchase.payeeAddress("   ").build())).containsExactly("payeeAddressPresentForPurchase");
        assertThat(violatedFields(purchase.payeeAddress("Beispielweg 5, 12043 Berlin").build())).isEmpty();
    }

    @Test
    void generalReceiptDoesNotRequireAddress() {
        assertThat(violatedFields(validReceipt().payeeAddress(null).build())).isEmpty();
    }

    @Test
    void payeeNameIsLimitedTo200Characters() {
        assertThat(violatedFields(validReceipt().payeeName("a".repeat(200)).build())).isEmpty();
        assertThat(violatedFields(validReceipt().payeeName("a".repeat(201)).build())).containsExactly("payeeName");
    }

    @Test
    void blankTextCountsAsMissing() {
        assertThat(violatedFields(validReceipt().reason("  ").build())).containsExactly("reason");
    }
}
