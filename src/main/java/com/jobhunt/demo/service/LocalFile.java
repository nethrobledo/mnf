package com.jobhunt.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
@Component
public class LocalFile implements FileStrategy {

    @Value("${user.dir}/ingredients.json")
    private String fileName;
    private static final ObjectMapper mapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ArrayList<LinkedHashMap> getIngredients() {
        logger.info("About to get ingredients " + fileName);

        ArrayList<LinkedHashMap> ingredients = null;
        File file = new File(fileName);

        try {
            LinkedHashMap linkedHashMap = mapper.readValue(file, LinkedHashMap.class);
            ingredients = (ArrayList) linkedHashMap.get("ingredients");
        } catch (IOException e) {
            logger.error("Error while parsing " + e.getMessage());
        }
        return ingredients;
    }

    @Override
    public ArrayList<LinkedHashMap> getRecipes() {
        ArrayList<LinkedHashMap> recipes = null;

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/recipes.json")) {
            LinkedHashMap linkedHashMap = mapper.readValue(inputStream, LinkedHashMap.class);
            recipes = (ArrayList) linkedHashMap.get("recipes");
        } catch (IOException e) {
            logger.error("Error while parsing " + e.getMessage());
        }
        return recipes;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
