package com.jobhunt.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RecipeResponse {

    private String title;
    private List<String> ingredients = new ArrayList<>();

}
