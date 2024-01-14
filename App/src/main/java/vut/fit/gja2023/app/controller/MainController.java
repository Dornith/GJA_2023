package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vut.fit.gja2023.app.Layout;
import vut.fit.gja2023.app.View;
import vut.fit.gja2023.app.service.SystemManagerService;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SystemManagerService systemManagerService;

    @RequestMapping("/")
    public String index(Model model) {
        model.setTargetView(View.MAIN);
        return Layout.DEFAULT.toString();
    }
}
