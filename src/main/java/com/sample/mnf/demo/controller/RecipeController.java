package com.sample.mnf.demo.controller;

import com.rezdy.recipes.exception.NotFoundException;
import com.sample.mnf.demo.model.Ingredient;
import com.sample.mnf.demo.model.Recipe;
import com.sample.mnf.demo.model.RecipeResponse;
import com.sample.mnf.demo.service.IngredientService;
import com.sample.mnf.demo.service.RecipeService;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayDeque;

@RestController
public class RecipeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String INGREDIENTS_ERROR_MESSAGE = "Missing ingredients";
    private static final String RECIPES_ERROR_MESSAGE = "Missing recipes";
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @Autowired
    public RecipeController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/lunch")
    @ResponseBody
    public ArrayDeque<RecipeResponse> getRecipes() {
        logger.info("Start GET lunch....");

        String fileName = System.getProperty("user.dir") + "/src/ingredients.json";

        // Need to process ingredients first as it is needed for recipe consolidation
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.convertJsonToBean(fileName);

        if (ingredientHashMap == null || ingredientHashMap.isEmpty()) {
            logger.error(INGREDIENTS_ERROR_MESSAGE);
            throw new NotFoundException(INGREDIENTS_ERROR_MESSAGE);
        }

        List<Recipe> recipeList = recipeService.convertJsonToBean(ingredientHashMap);

        if (recipeList == null || recipeList.isEmpty()) {
            logger.error(RECIPES_ERROR_MESSAGE);
            throw new NotFoundException(RECIPES_ERROR_MESSAGE);
        }

        ArrayDeque<RecipeResponse> arrayDeque = recipeService.filterResponseAndSort(recipeList);

        logger.info("End GET lunch....");

        return arrayDeque;
    }

}
