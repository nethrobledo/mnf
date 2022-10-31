package com.jobhunt.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Recipe {

    private String title;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Ingredient> expiredIngredients = new ArrayList<>();
    private List<Ingredient> bestBeforeIngredients = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(" title:");
        stringBuilder.append(title);
        return stringBuilder.toString();
    }
}
