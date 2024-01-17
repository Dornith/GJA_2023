package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vut.fit.gja2023.app.service.UserService;

@ControllerAdvice
@RequiredArgsConstructor
public class UserControllerAdvice {
    private final UserService userService;

    @ModelAttribute
    public void addLoggedInUserToModel(Model model) {
        var user = userService.getLoggedInUser();
        user.ifPresent(userBo -> model.setLoggedInUser(userBo));
    }
}
