package com.jobhunt.demo.service;

import com.jobhunt.demo.exception.NotFoundException;
import com.jobhunt.demo.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private static final String INGREDIENTS_ERROR_MESSAGE = "Missing ingredients";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final FileService fileService;

    /**
     * Converts json file into bean object
     * ingredients.json file is created on the fly during server startup
     * assuming at this point that the file has been created successfully
     */
    public HashMap<String, Ingredient> getIngredients() {

        HashMap<String, Ingredient> resultMap = new HashMap<>();
        ArrayList<LinkedHashMap> ingredients = fileService.getIngredientsFromLocal();

        if (ingredients == null || ingredients.isEmpty()) {
            logger.error(INGREDIENTS_ERROR_MESSAGE);
            throw new NotFoundException(INGREDIENTS_ERROR_MESSAGE);
        }

        for (LinkedHashMap ingredientsMap : ingredients) {
            Ingredient ingredient = new Ingredient();
            ingredient.setBestBefore(LocalDate.parse((String) ingredientsMap.get("best-before"), formatter));
            ingredient.setUseBy(LocalDate.parse((String) ingredientsMap.get("use-by"), formatter));
            ingredient.setTitle((String) ingredientsMap.get("title"));

            resultMap.put((String) ingredientsMap.get("title"), ingredient);
        }

        logger.info("Finish converting JSON ingredients to bean...");

        return resultMap;
    }

}
