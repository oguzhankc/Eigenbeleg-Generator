package htw.webtech2026.Webtech2026.web;

import htw.webtech2026.Webtech2026.model.Receipt;
import htw.webtech2026.Webtech2026.model.ReceiptRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptRepository repository;

    public ReceiptController(ReceiptRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Receipt> getReceipts() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Receipt createReceipt(@Valid @RequestBody Receipt receipt) {
        // Vom System gesetzte Felder überschreiben, was der Client mitschickt; eine mitgeschickte id würde sonst einen fremden Beleg überschreiben
        receipt.setId(null);
        receipt.setNumber("EB-%d-%04d".formatted(Year.now().getValue(), repository.nextNumber()));
        receipt.setCreatedAt(Instant.now());
        receipt.setOwnerId(null);        // bis zum Login (finale Abgabe)
        receipt.setIssuerName(null);     // bis zu den Firmendaten (finale Abgabe)
        receipt.setIssuerAddress(null);
        return repository.save(receipt);
    }
}
