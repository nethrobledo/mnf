package com.jobhunt.demo.controller;

import com.jobhunt.demo.exception.NotFoundException;
import com.jobhunt.demo.model.Ingredient;
import com.jobhunt.demo.model.RecipeResponse;
import com.jobhunt.demo.service.FileService;
import com.jobhunt.demo.service.RecipeService;
import com.jobhunt.demo.service.IngredientService;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String INGREDIENTS_ERROR_MESSAGE = "Missing ingredients";
    private static final String RECIPES_ERROR_MESSAGE = "Missing recipes";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final FileService fileService;

    @Autowired
    public RecipeController(RecipeService recipeService, IngredientService ingredientService, FileService fileService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.fileService = fileService;
    }

    @GetMapping("/lunch")
    @ResponseBody
    public ArrayDeque<RecipeResponse> getRecipes() {
        logger.info("Start GET lunch....");

        // Need to process ingredients first as it is needed for recipe consolidation
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.getIngredients();
        if (ingredientHashMap == null || ingredientHashMap.isEmpty()) {
            logger.error(INGREDIENTS_ERROR_MESSAGE);
            throw new NotFoundException(INGREDIENTS_ERROR_MESSAGE);
        }

        ArrayDeque<RecipeResponse> arrayDeque = recipeService.getRecipes(ingredientHashMap);
        if (arrayDeque == null || arrayDeque.isEmpty()) {
            logger.error(RECIPES_ERROR_MESSAGE);
            throw new NotFoundException(RECIPES_ERROR_MESSAGE);
        }

        logger.info("End GET lunch....");

        return arrayDeque;
    }

}
