package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vut.fit.gja2023.app.enums.Layout;
import vut.fit.gja2023.app.enums.UserRole;
import vut.fit.gja2023.app.enums.View;
import vut.fit.gja2023.app.service.UserService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        model.setTargetView(View.MAIN);
        return Layout.DEFAULT.getValue();
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (userService.isLoggedIn()) {
            return "redirect:/";
        }

        model.setTargetView(View.LOGIN);
        return Layout.EMPTY.getValue();
    }
}
