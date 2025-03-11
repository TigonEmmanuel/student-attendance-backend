package com.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/protected-endpoint")
    public String protectedEndpoint() {
        return "Access Granted to Admin!";
    }
}
