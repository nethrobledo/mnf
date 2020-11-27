package com.sample.mnf.demo.controller;

import com.sample.mnf.demo.model.Post;
import com.sample.mnf.demo.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    private final ApiService apiService;

    @Autowired
    ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/api")
    public Mono<Post> get() {
        return apiService.getApis();
    }
}
