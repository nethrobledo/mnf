package com.jobhunt.demo.controller;

import com.jobhunt.demo.model.Ingredient;
import com.jobhunt.demo.model.RecipeResponse;
import com.jobhunt.demo.service.DataService;
import com.jobhunt.demo.service.IngredientService;
import com.jobhunt.demo.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayDeque;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class RecipeIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeService recipeService;
    @MockBean
    private IngredientService ingredientService;

    @MockBean
    private DataService dataService;

    @Test
    @WithMockUser(username="spring")
    public void getLunch()
            throws Exception {

        HashMap<String, Ingredient> ingredientHashMap = new HashMap<>();
        ingredientHashMap.put("test",new Ingredient());
        ArrayDeque<RecipeResponse> responses = new ArrayDeque<>();
        responses.add(new RecipeResponse());

        when(recipeService.getRecipes(ingredientHashMap)).thenReturn(responses);
        when(ingredientService.getIngredients()).thenReturn(ingredientHashMap);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/lunch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
