package htw.webtech2026.Webtech2026.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

// Wandelt abgelehnte @Valid-Eingaben in HTTP 400 mit {"feld": "Meldung"} um, damit das Frontend die Meldung am Feld zeigt.
@RestControllerAdvice
public class ValidationErrorHandler {

    // Die zwei @AssertTrue-Regeln in Receipt heißen wie ihre Methode; das Frontend braucht das Feld, an dem der Fehler steht
    private static final Map<String, String> RULE_TO_FIELD = Map.of(
            "issueDateNotBeforePaymentDate", "issueDate",
            "payeeAddressPresentForPurchase", "payeeAddress");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(MethodArgumentNotValidException e) {
        Map<String, String> errors = new TreeMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(RULE_TO_FIELD.getOrDefault(error.getField(), error.getField()), error.getDefaultMessage()));
        return errors;
    }
}
