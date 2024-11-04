package com.example.loyalty.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthController {
    @GetMapping(value = "/manage/health")
    private ResponseEntity isAlive(){
        return ResponseEntity.status(200).build();
    }
}
