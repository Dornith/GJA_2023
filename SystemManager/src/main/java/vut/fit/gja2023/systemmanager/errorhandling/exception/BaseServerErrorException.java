package vut.fit.gja2023.systemmanager.errorhandling.exception;

import org.springframework.http.HttpStatus;

public class BaseServerErrorException extends BaseRestException {

    public BaseServerErrorException(String... args) {
        super(args);
    }

    @Override
    public String getCode() {
        return "internal";
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
