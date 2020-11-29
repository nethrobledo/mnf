package com.sample.mnf.demo.service;

import com.sample.mnf.demo.model.Ingredient;
import com.sample.mnf.demo.model.Recipe;
import com.sample.mnf.demo.model.RecipeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.Assert.assertEquals;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @InjectMocks
    private DummyDataService dummyDataService;

    @InjectMocks
    private IngredientService ingredientService;

    @InjectMocks
    private RecipeService recipeService;

    private static final String fileName = System.getProperty("user.dir") + "/unit_test.json";

    @Test
    public void testOneMonth() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Bread");
        dummyDataService.createUnitTestFile(fileName, ingredients, -30, -30);
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.convertJsonToBean(fileName);
        List<Recipe> recipeList = recipeService.convertJsonToBean(ingredientHashMap);
        ArrayDeque<RecipeResponse> arrayDeque = recipeService.filterResponseAndSort(recipeList);
        assertEquals(arrayDeque.size(), 3);
    }

    @Test
    public void testUseByToday() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Lettuce");
        dummyDataService.createUnitTestFile(fileName, ingredients, 0, 0);
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.convertJsonToBean(fileName);
        List<Recipe> recipeList = recipeService.convertJsonToBean(ingredientHashMap);
        ArrayDeque<RecipeResponse> arrayDeque = recipeService.filterResponseAndSort(recipeList);
        assertEquals(arrayDeque.size(), 5);
    }

    @Test
    public void testTomorrow() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Spinach");
        ingredients.add("Cheese");
        ingredients.add("Bread");
        dummyDataService.createUnitTestFile(fileName, ingredients, 1, 1);
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.convertJsonToBean(fileName);
        List<Recipe> recipeList = recipeService.convertJsonToBean(ingredientHashMap);
        ArrayDeque<RecipeResponse> arrayDeque = recipeService.filterResponseAndSort(recipeList);
        assertEquals(arrayDeque.size(), 5);
    }

    @Test
    public void testExpiredYesterday() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Spinach");
        ingredients.add("Cheese");
        ingredients.add("Bread");
        dummyDataService.createUnitTestFile(fileName, ingredients, 1, -1);
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.convertJsonToBean(fileName);
        List<Recipe> recipeList = recipeService.convertJsonToBean(ingredientHashMap);
        ArrayDeque<RecipeResponse> arrayDeque = recipeService.filterResponseAndSort(recipeList);
        assertEquals(arrayDeque.size(), 2);
    }

    @Test
    public void testSortLast() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Hotdog Bun");
        dummyDataService.createUnitTestFile(fileName, ingredients, -1, 0);
        HashMap<String, Ingredient> ingredientHashMap = ingredientService.convertJsonToBean(fileName);
        List<Recipe> recipeList = recipeService.convertJsonToBean(ingredientHashMap);
        ArrayDeque<RecipeResponse> arrayDeque = recipeService.filterResponseAndSort(recipeList);
        assertEquals(arrayDeque.size(), 5);

        RecipeResponse last = arrayDeque.getLast();
        assertEquals(last.getTitle(), "Hotdog");
    }
}
