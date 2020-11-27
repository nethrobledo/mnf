package com.jobhunt.demo;

import com.jobhunt.demo.service.DataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(DataService dataService) {
        return args -> {
            dataService.createJsonTestFile("ingredients.json");
        };
    }
}
