package com.vvsgk.banking_transaction_engine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    public HomeController() {
        System.out.println("Home Controller Object Created");
    }

    @GetMapping("/")
    public String home() {
        return "Hello Banking Engine V4";
    }
}
