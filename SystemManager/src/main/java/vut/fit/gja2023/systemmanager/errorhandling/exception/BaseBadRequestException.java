package vut.fit.gja2023.systemmanager.errorhandling.exception;

import org.springframework.http.HttpStatus;

public class BaseBadRequestException extends BaseRestException {

    public BaseBadRequestException(String... args) {
        super(args);
    }

    @Override
    public String getCode() {
        return "badRequest";
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
