package vut.fit.gja2023.systemmanager.errorhandling.exception;

import org.springframework.http.HttpStatus;

public class BaseNotFoundException extends BaseRestException {

    public BaseNotFoundException(String... args) {
        super(args);
    }

    @Override
    public String getCode() {
        return "notFound";
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

}
