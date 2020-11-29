package com.sample.mnf.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rezdy.recipes.dto.RecipeResponse;
import com.sample.mnf.demo.model.Ingredient;
import com.sample.mnf.demo.model.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Class responsible for handling any logic related to recipes
 * Used by both controller and unit tests
 */
@Service
public class RecipeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Converts json file into bean object
     * Recipe json file in this case has static values
     *
     * @param ingredientHashMap
     * @return List of Recipe
     */
    public List<Recipe> convertJsonToBean(HashMap<String, Ingredient> ingredientHashMap) {
        if (ingredientHashMap == null) return null;

        logger.info("Start converting JSON recipes to bean...");

        ObjectMapper mapper = new ObjectMapper();
        List<Recipe> consolidatedRecipeList = new ArrayList<>();

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/recipes.json")) {
            LinkedHashMap linkedHashMap = mapper.readValue(inputStream, LinkedHashMap.class);
            ArrayList<LinkedHashMap> recipes = (ArrayList) linkedHashMap.get("recipes");

            for (LinkedHashMap recipeLinkedHashMap: recipes) {
                Recipe recipe = createRecipe(recipeLinkedHashMap, ingredientHashMap);
                consolidatedRecipeList.add(recipe);
            }

        } catch (IOException e) {
            logger.error("Error while parsing " + e.getMessage());
        }
        logger.info("Finish converting JSON recipes to bean...");
        return consolidatedRecipeList;
    }

    /**
     * Consolidate ingredients and recipe json file into one bean
     * with expired ingredients and best before list of ingredients
     *
     * @param recipeLinkHashMap
     * @param ingredientHashMap
     *
     * @return a Recipe object with expired and best before list as well
     */
    private Recipe createRecipe(LinkedHashMap recipeLinkHashMap, HashMap<String, Ingredient> ingredientHashMap) {
        Recipe recipe = new Recipe();
        recipe.setTitle((String) recipeLinkHashMap.get("title"));

        ArrayList<String> ingredients = (ArrayList) recipeLinkHashMap.get("ingredients");
        LocalDate now = LocalDate.now();

        for (String strIngredient : ingredients) {
            Ingredient ingredient = ingredientHashMap.get(strIngredient);
            if (ingredient == null) continue;

            if (now.isAfter(ingredient.getUseBy())) {
                recipe.getExpiredIngredients().add(ingredient);
            }
            if (now.isAfter(ingredient.getBestBefore())) {
                recipe.getBestBeforeIngredients().add(ingredient);
            }
            recipe.getIngredients().add(ingredient);
        }
        return recipe;
    }

    /**
     * Used by both controller and unit tests
     *
     * 1. Remove from the list any recipe with at least one expired ingredient
     * 2. Add to the bottom of the JSON response object the recipe where an ingredient is past its ​best-before​ date AND is still within its date
     * 3. Add to the top of the JSON response object the recipe where an ingredient is NOT past its ​best-before​ date AND is still within its date
     *
     * @param recipeList
     *
     * @return
     */
    public ArrayDeque<RecipeResponse> filterResponseAndSort(List<Recipe> recipeList) {
        ArrayDeque<RecipeResponse> arrayDeque = new ArrayDeque<>();

        for (Recipe recipe: recipeList) {
            // Collections.disjoint returns true if the two specified collections have no elements in common
            // Remove from the list any recipe with at least one expired ingredient
            // The list of expired ingredients has been generated during conversion of Json to Bean
            if (!Collections.disjoint(recipe.getIngredients(), recipe.getExpiredIngredients())) {
                logger.info("Found expired ingredients in [" + recipe.getTitle() + "]");
                continue;
            }
            RecipeResponse recipeResponse = createResponse(recipe);

            // Collections.disjoint returns true if the two specified collections have no elements in common
            // Add to the bottom where an ingredient is past its ​best-before​ date else add to the top
            // The list of best before ingredients has been generated during conversion of Json to Bean
            if (Collections.disjoint(recipe.getIngredients(), recipe.getBestBeforeIngredients())) {
                logger.info("Adding first [" + recipe.getTitle() + "]");
                arrayDeque.addFirst(recipeResponse);
            } else {
                logger.info("Adding last [" + recipe.getTitle() + "]");
                arrayDeque.addLast(recipeResponse);
            }
        }
        return arrayDeque;
    }

    /**
     *
     * @param recipeBean
     *
     * @return
     */
    private RecipeResponse createResponse(Recipe recipeBean) {
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setTitle(recipeBean.getTitle());

        recipeBean.getIngredients().forEach(s -> recipeResponse.getIngredients().add(s.getTitle()));

        return recipeResponse;
    }

}
