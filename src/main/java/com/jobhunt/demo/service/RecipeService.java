package com.jobhunt.demo.service;

import com.jobhunt.demo.model.Recipe;
import com.jobhunt.demo.model.RecipeResponse;
import com.jobhunt.demo.exception.NotFoundException;
import com.jobhunt.demo.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RecipeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String RECIPES_ERROR_MESSAGE = "Missing recipes";
    private final FileService fileService;

    @Autowired
    public RecipeService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Retrieves available recipes based from ingredients
     */
    public ArrayDeque<RecipeResponse> getRecipes(HashMap<String, Ingredient> ingredientHashMap) {
        if (ingredientHashMap == null) {
            return null;
        }

        List<Recipe> consolidatedRecipeList = new ArrayList<>();
        ArrayList<LinkedHashMap> recipes = fileService.getRecipesFromLocal();

        for (LinkedHashMap recipeLinkedHashMap : recipes) {
            Recipe recipe = createRecipe(recipeLinkedHashMap, ingredientHashMap);
            consolidatedRecipeList.add(recipe);
        }

        if (consolidatedRecipeList == null || consolidatedRecipeList.isEmpty()) {
            logger.error(RECIPES_ERROR_MESSAGE);
            throw new NotFoundException(RECIPES_ERROR_MESSAGE);
        }
        logger.info("Finish converting JSON recipes to bean...");

        return filterResponseAndSort(consolidatedRecipeList);

    }

    /**
     * Consolidate ingredients and recipe json file into one bean
     * with expired ingredients and best before list of ingredients
     *
     * @return a Recipe object with expired and best before list as well
     */
    Recipe createRecipe(LinkedHashMap recipeLinkHashMap, HashMap<String, Ingredient> ingredientHashMap) {
        Recipe recipe = new Recipe();
        recipe.setTitle((String) recipeLinkHashMap.get("title"));

        ArrayList<String> ingredients = (ArrayList) recipeLinkHashMap.get("ingredients");
        LocalDate now = LocalDate.now();

        for (String strIngredient : ingredients) {
            Ingredient ingredient = ingredientHashMap.get(strIngredient);
            if (ingredient == null) {
                continue;
            }

            if (ingredient.getUseBy() != null && (now.isEqual(ingredient.getUseBy())
                    || now.isAfter(ingredient.getUseBy()))) {
                recipe.getExpiredIngredients().add(ingredient);
            }

            if (ingredient.getBestBefore() != null && (now.isEqual(ingredient.getBestBefore())
                    || now.isAfter(ingredient.getBestBefore()))) {
                recipe.getBestBeforeIngredients().add(ingredient);
            }
            recipe.getIngredients().add(ingredient);
        }
        return recipe;
    }

    /**
     * 1. Remove from the list any recipe with at least one expired ingredient
     * 2. Add to the bottom the recipe where an ingredient is past its best-before date AND is still within its date
     * 3. Add to the top the recipe where an ingredient is NOT past its best-before date AND is still within its date
     */
    ArrayDeque<RecipeResponse> filterResponseAndSort(List<Recipe> recipeList) {
        ArrayDeque<RecipeResponse> arrayDeque = new ArrayDeque<>();

        for (Recipe recipe : recipeList) {
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

    private RecipeResponse createResponse(Recipe recipeBean) {
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setTitle(recipeBean.getTitle());

        recipeBean.getIngredients().forEach(s -> recipeResponse.getIngredients().add(s.getTitle()));

        return recipeResponse;
    }

}
