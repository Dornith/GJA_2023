package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vut.fit.gja2023.app.Layout;
import vut.fit.gja2023.app.View;

@Controller
@RequiredArgsConstructor
public class MainController {

    @RequestMapping("/")
    public String index(Model model) {
        model.setTargetView(View.MAIN);
        return Layout.DEFAULT.toString();
    }
}
