package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vut.fit.gja2023.app.Layout;
import vut.fit.gja2023.app.View;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(Exception ex, Model model) {
        model.setTargetView(View.ERROR);

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", ex.getMessage());

        return Layout.EMPTY.toString();
    }
}
