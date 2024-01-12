package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vut.fit.gja2023.app.Layout;

@Controller
@RequiredArgsConstructor
public class MainController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute(Layout.VIEW, "index");

        return Layout.DEFAULT_LAYOUT;
    }
}
