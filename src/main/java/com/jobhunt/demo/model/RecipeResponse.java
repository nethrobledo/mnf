package com.jobhunt.demo.model;

import java.util.ArrayList;
import java.util.List;

public class RecipeResponse {

    private String title;
    private List<String> ingredients = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
