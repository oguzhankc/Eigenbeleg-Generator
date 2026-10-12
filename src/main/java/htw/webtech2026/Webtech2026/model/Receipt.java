package htw.webtech2026.Webtech2026.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private UUID id;
    private String number;
    private ReceiptType type;
    private LocalDate issueDate;
    private LocalDate paymentDate;
    private String payeeName;
    private String payeeAddress;
    private String description;
    private Long amountCents;
    private VatRate vatRate;
    private String reason;
    private PaymentMethod paymentMethod;
    private Instant createdAt;
    private UUID ownerId;   // bleibt in M1 null, wird erst mit Login relevant
}
