package vut.fit.gja2023.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vut.fit.gja2023.app.service.SystemManagerService;

@Controller
@RequiredArgsConstructor
public class MainController {

    SystemManagerService systemManagerService;

    @Autowired
    public MainController(SystemManagerService systemManagerService) {
        this.systemManagerService = systemManagerService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
