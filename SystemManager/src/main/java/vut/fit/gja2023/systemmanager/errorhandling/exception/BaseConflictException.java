package vut.fit.gja2023.systemmanager.errorhandling.exception;

import org.springframework.http.HttpStatus;

public class BaseConflictException extends BaseRestException {

    public BaseConflictException(String... args) {
        super(args);
    }

    @Override
    public String getCode() {
        return "conflict";
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }
}
