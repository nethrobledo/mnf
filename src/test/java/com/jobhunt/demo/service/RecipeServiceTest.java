package com.jobhunt.demo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jobhunt.demo.exception.NotFoundException;
import com.jobhunt.demo.model.Ingredient;
import com.jobhunt.demo.model.Recipe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void testMissingRecipes() {
        assertThrows(NotFoundException.class, () -> recipeService.getRecipes(new HashMap<>()));
    }

    @Test
    public void testCreateRecipe_bestBefore() {
        LinkedHashMap linkedHashMapArrayList = new LinkedHashMap<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setTitle("Ham");
        ingredient.setBestBefore(LocalDate.now().minusDays(1));

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Ham");

        linkedHashMapArrayList.put("title","Fry up");
        linkedHashMapArrayList.put("ingredients",stringArrayList);

        HashMap<String, Ingredient> ingredientHashMap = new HashMap<>();
        ingredientHashMap.put("Ham", ingredient);

        Recipe recipe = recipeService.createRecipe(linkedHashMapArrayList, ingredientHashMap);
        Assertions.assertEquals(recipe.getBestBeforeIngredients().size(), 1);

    }
}
