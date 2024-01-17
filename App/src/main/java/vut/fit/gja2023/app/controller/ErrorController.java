package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vut.fit.gja2023.app.enums.Layout;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(Exception ex, Model model) {
        model.setErrorView("404", ex.getMessage() + "\n" + ex.getStackTrace().toString());
        return Layout.EMPTY.getValue();
    }
}
