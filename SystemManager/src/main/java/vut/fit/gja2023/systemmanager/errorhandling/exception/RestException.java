package vut.fit.gja2023.systemmanager.errorhandling.exception;

import org.springframework.http.HttpStatus;

public interface RestException {

    String getCode();

    HttpStatus status();
}
