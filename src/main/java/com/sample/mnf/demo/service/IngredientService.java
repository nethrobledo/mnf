package com.sample.mnf.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.mnf.demo.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Service
public class IngredientService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Converts json file into bean object
     * ingredients.json file is created on the fly during server startup
     * assuming at this point that the file has been created successfully
     *
     * @return
     */
    public HashMap<String, Ingredient> convertJsonToBean(final String fileName) {
        logger.info("Start converting JSON ingredients to bean...");

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        HashMap<String, Ingredient> resultMap = new HashMap<>();

        try {
            LinkedHashMap linkedHashMap = mapper.readValue(file, LinkedHashMap.class);
            ArrayList<LinkedHashMap> ingredients = (ArrayList) linkedHashMap.get("ingredients");

            for (LinkedHashMap ingredientsMap:ingredients) {
                Ingredient ingredient = new Ingredient();
                ingredient.setBestBefore(LocalDate.parse((String) ingredientsMap.get("best-before"), formatter));
                ingredient.setUseBy(LocalDate.parse((String) ingredientsMap.get("use-by"), formatter));
                ingredient.setTitle((String) ingredientsMap.get("title"));

                resultMap.put((String) ingredientsMap.get("title"), ingredient);
            }

        } catch (IOException e){
            logger.error("Error while parsing " + e.getMessage());
        }
        logger.info("Finish converting JSON ingredients to bean...");

        return resultMap;
    }

}
