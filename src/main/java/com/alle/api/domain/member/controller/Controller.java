package com.alle.api.domain.member.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/favicon.ico")
    public String test(){
        return null;
    }
}
