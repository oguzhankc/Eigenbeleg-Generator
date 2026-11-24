package htw.webtech2026.Webtech2026.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

// Spring Data erzeugt die Umsetzung zur Laufzeit; save, findAll usw. erbt das Interface von JpaRepository.
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

    List<Receipt> findAllByOrderByCreatedAtDesc();

    // Nächster Wert der Sequenz aus schema.sql; die Datenbank vergibt jeden Wert nur einmal, auch bei gleichzeitigen Anfragen
    @Query(value = "SELECT nextval('receipt_number_seq')", nativeQuery = true)
    long nextNumber();
}
