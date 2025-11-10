package ua.nure.st.kpp.example.demo.controller;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainMenuController {
    Logger log = Logger.getLogger(MainMenuController.class.getName());

    @RequestMapping("/")
    public String showMainPage() {
        return "MainMenu";
    }

}
