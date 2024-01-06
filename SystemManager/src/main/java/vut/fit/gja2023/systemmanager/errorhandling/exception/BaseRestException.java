package vut.fit.gja2023.systemmanager.errorhandling.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Base exception class, used as parent class for all other types of exceptions. Used in
 * {@link vut.fit.gja2023.systemmanager.errorhandling.GlobalExceptionHandler} for centralized error handling. By
 * extending this class, it is required to define status code and code of the error.
 */
@Getter
@AllArgsConstructor
public abstract class BaseRestException extends RuntimeException implements RestException {

    private Object[] args;

    protected BaseRestException(Throwable cause) {
        super(cause);
    }

}
