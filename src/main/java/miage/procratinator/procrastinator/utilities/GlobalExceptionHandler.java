package miage.procratinator.procrastinator.utilities;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions IllegalArgumentException levées dans l'application et retourne
     * une réponse d'erreur détaillée.
     *
     * @param ex l'exception IllegalArgumentException qui a été levée
     * @return une ResponseEntity contenant un statut 400 Bad Request et le message de l'exception
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
