package htw.webtech2026.Webtech2026.web;

import htw.webtech2026.Webtech2026.model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @GetMapping
    public List<Receipt> getReceipts() {
        return List.of(
                Receipt.builder()
                        .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                        .number("EB-2026-0001")
                        .type(ReceiptType.ALLGEMEIN)
                        .issueDate(LocalDate.of(2026, 10, 1))
                        .paymentDate(LocalDate.of(2026, 9, 30))
                        .payeeName("Parkhaus Mitte GmbH")
                        .payeeAddress("Musterstraße 1, 10115 Berlin")
                        .description("Parkgebühr Kundentermin")
                        .amountCents(450L)
                        .vatRate(VatRate.REGELSATZ_19)
                        .reason("Parkautomat gab keine Quittung aus")
                        .paymentMethod(PaymentMethod.BAR)
                        .createdAt(Instant.parse("2026-10-01T09:15:00Z"))
                        .build(),
                Receipt.builder()
                        .id(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                        .number("EB-2026-0002")
                        .type(ReceiptType.ANKAUF)
                        .issueDate(LocalDate.of(2026, 10, 3))
                        .paymentDate(LocalDate.of(2026, 10, 3))
                        .payeeName("Max Mustermann")
                        .payeeAddress("Beispielweg 5, 12043 Berlin")
                        .description("Gebrauchtes Notebook, Lenovo ThinkPad T480")
                        .amountCents(18000L)
                        .vatRate(VatRate.OHNE_UST)
                        .reason("Ankauf von Privatperson ohne Rechnung")
                        .paymentMethod(PaymentMethod.BAR)
                        .createdAt(Instant.parse("2026-10-03T14:30:00Z"))
                        .build(),
                Receipt.builder()
                        .id(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                        .number("EB-2026-0003")
                        .type(ReceiptType.ALLGEMEIN)
                        .issueDate(LocalDate.of(2026, 10, 5))
                        .paymentDate(LocalDate.of(2026, 10, 4))
                        .payeeName("Restaurant Beispiel")
                        .payeeAddress("Hauptstraße 10, 10827 Berlin")
                        .description("Trinkgeld Geschäftsessen")
                        .amountCents(500L)
                        .vatRate(VatRate.OHNE_UST)
                        .reason("Trinkgeld wird nicht quittiert")
                        .paymentMethod(PaymentMethod.BAR)
                        .createdAt(Instant.parse("2026-10-05T08:00:00Z"))
                        .build()
        );
    }
}
