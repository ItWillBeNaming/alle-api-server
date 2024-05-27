package com.alle.api.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempController {

    @GetMapping("/favicon")
    public String favicon() {
        return null;
    }
    @GetMapping("/.")
    public String favicon2() {
        return null;
    }
}
