package vut.fit.gja2023.systemmanager.errorhandling.error;

import java.time.OffsetDateTime;
import java.util.UUID;


/**
 * Main DTO class for rest error response returned by handlers defined in GlobalExceptionHandler with corresponding status code
 */
public record RestError(UUID uuid, RestErrorMessage error, OffsetDateTime timestamp) {
}