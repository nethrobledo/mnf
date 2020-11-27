package com.jobhunt.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String title;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Ingredient> expiredIngredients = new ArrayList<>();
    private List<Ingredient> bestBeforeIngredients = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getExpiredIngredients() {
        return expiredIngredients;
    }

    public void setExpiredIngredients(List<Ingredient> expiredIngredients) {
        this.expiredIngredients = expiredIngredients;
    }

    public List<Ingredient> getBestBeforeIngredients() {
        return bestBeforeIngredients;
    }

    public void setBestBeforeIngredients(List<Ingredient> bestBeforeIngredients) {
        this.bestBeforeIngredients = bestBeforeIngredients;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(" title:");
        stringBuilder.append(title);
        return stringBuilder.toString();
    }
}
