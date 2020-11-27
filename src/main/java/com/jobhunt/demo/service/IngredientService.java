package com.jobhunt.demo.service;

import com.jobhunt.demo.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.springframework.util.StringUtils;

@Service
public class IngredientService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final FileService fileService;

    @Autowired
    public IngredientService(FileService fileService) {
        this.fileService = fileService;

        if (StringUtils.isEmpty(fileService.getFileName())) {
            this.fileService.setFileName(System.getProperty("user.dir") + "/ingredients.json");
        }
    }

    /**
     * Converts json file into bean object
     * ingredients.json file is created on the fly during server startup
     * assuming at this point that the file has been created successfully
     */
    public HashMap<String, Ingredient> getIngredients() {

        HashMap<String, Ingredient> resultMap = new HashMap<>();
        ArrayList<LinkedHashMap> ingredients = fileService.getIngredientsFromLocal();

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
