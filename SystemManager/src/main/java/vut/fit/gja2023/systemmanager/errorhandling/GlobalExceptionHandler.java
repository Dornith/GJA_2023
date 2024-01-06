package vut.fit.gja2023.systemmanager.errorhandling;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vut.fit.gja2023.systemmanager.errorhandling.error.RestError;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseRestException;
import vut.fit.gja2023.systemmanager.errorhandling.mapper.RestErrorMapper;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final RestErrorMapper errorMapper;

    /**
     * Common handler for all new exceptions that inherit from {@link BaseRestException}.
     */
    @ExceptionHandler(BaseRestException.class)
    public ResponseEntity<RestError> handleException(@NonNull BaseRestException ex) {
        RestError restError = errorMapper.map(ex, Locale.ENGLISH);
        log.error("Handling exception, uuid={}", restError.uuid(), ex);
        return ResponseEntity.status(ex.status()).body(restError);
    }
}
