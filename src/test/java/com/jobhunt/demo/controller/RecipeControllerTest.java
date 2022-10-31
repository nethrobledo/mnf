package com.jobhunt.demo.controller;

import com.jobhunt.demo.client.RecipeFileClient;
import com.jobhunt.demo.exception.NotFoundException;
import com.jobhunt.demo.model.RecipeResponse;
import com.jobhunt.demo.service.DataService;
import com.jobhunt.demo.service.FileService;
import com.jobhunt.demo.service.RecipeService;
import com.jobhunt.demo.service.IngredientService;
import java.util.ArrayDeque;
import java.util.ArrayList;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("integration")
@ExtendWith(MockitoExtension.class)

public class RecipeControllerTest {
    private RecipeService recipeService;
    private FileService fileService;
    private IngredientService ingredientService;
    private RecipeController recipeController;
    private RecipeFileClient recipeFileClient;

    @InjectMocks
    private DataService dataService;

    private static final String fileName = System.getProperty("user.dir") + "/src/test/ingredients.json";

    @BeforeEach
    public void init() {
        fileService = new FileService(recipeFileClient);
        fileService.setFileName(fileName);
        recipeService = new RecipeService(fileService);
        ingredientService = new IngredientService(fileService);
        recipeController = new RecipeController(recipeService,ingredientService);
    }

    @Test
    public void testExpiredBreadOneMonth() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Bread");
        dataService.createUnitTestFile(fileName, ingredients, -30, -30);
        
        ArrayDeque<RecipeResponse> arrayDeque = recipeController.getRecipes();
        Assertions.assertEquals(arrayDeque.size(), 3);
    }

    @Test
    public void testLettuceUseByToday() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Lettuce");
        dataService.createUnitTestFile(fileName, ingredients, 0, 0);

        ArrayDeque<RecipeResponse> arrayDeque = recipeController.getRecipes();
        Assertions.assertEquals(arrayDeque.size(), 4);
    }

    @Test
    public void testExpiredByTomorrow() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Spinach");
        ingredients.add("Cheese");
        ingredients.add("Bread");
        dataService.createUnitTestFile(fileName, ingredients, 1, 1);

        ArrayDeque<RecipeResponse> arrayDeque = recipeController.getRecipes();
        Assertions.assertEquals(arrayDeque.size(), 5);
    }

    @Test
    public void testExpiredYesterday() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Spinach");
        ingredients.add("Cheese");
        ingredients.add("Bread");
        dataService.createUnitTestFile(fileName, ingredients, 1, -1);

        ArrayDeque<RecipeResponse> arrayDeque = recipeController.getRecipes();
        Assertions.assertEquals(arrayDeque.size(), 2);
    }
    @Test
    public void testSortLast() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Hotdog Bun");
        dataService.createUnitTestFile(fileName, ingredients, -1, 0);

        ArrayDeque<RecipeResponse> arrayDeque = recipeController.getRecipes();
        Assertions.assertEquals(arrayDeque.size(), 4);

        RecipeResponse last = arrayDeque.getLast();
        Assertions.assertEquals(last.getTitle(), "Ham and Cheese Toastie");
    }

    @Test
    public void testErrorHandling() {
        FileService fileService = new FileService(recipeFileClient);
        fileService.setFileName("error.json");
        RecipeService recipeService = new RecipeService(fileService);
        IngredientService ingredientService = new IngredientService(fileService);
        RecipeController recipeController = new RecipeController(recipeService,ingredientService);

        Assertions.assertThrows(NotFoundException.class, () -> {
            recipeController.getRecipes();
        });

    }
}
