package cz.vut.fit.gja_2023.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class mainController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
