package com.jobhunt.demo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import com.jobhunt.demo.model.DummyIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Populates dummy test data during startup
 */
@Service
public class DataService {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final List<String> meatIngredients = List.of("Bacon");
    private static final List<String> saladIngredients = List.of("Lettuce", "Tomato", "Cucumber", "Beetroot", "Salad Dressing");
    private static final List<String> expiringIn3Days = List.of("Ham", "Cheese","Bread", "Butter",
            "Eggs", "Mushroom", "Milk", "Salt", "Pepper", "Spinach");
    private static final List<String> expiredYesterday = List.of("Hotdog Bun", "Sausage", "Ketchup", "Mustard");

    /**
     * Called from the server during startup to create dynamic json file depending on current date
     * during server startup
     *
     * @param fileName The name of the file to be created
     */
    public void createJsonTestFile(String fileName) {
        logger.info("Start populating test data for " + fileName);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(fileName)){
            List<DummyIngredient> ingredientsList = new ArrayList<>();

            expiringIn3Days.forEach(ingredient -> {
                DummyIngredient dummyIngredient = createDummyIngredient(ingredient, 3, 3);
                ingredientsList.add(dummyIngredient);
            });

            expiredYesterday.forEach(ingredient -> {
                DummyIngredient dummyIngredient = createDummyIngredient(ingredient, -1, -1);
                ingredientsList.add(dummyIngredient);
            });

            meatIngredients.forEach(ingredient -> {
                DummyIngredient dummyIngredient = createDummyIngredient(ingredient, -3, -3);
                ingredientsList.add(dummyIngredient);
            });

            saladIngredients.forEach(ingredient -> {
                DummyIngredient dummyIngredient = createDummyIngredient(ingredient, 0, 0);
                ingredientsList.add(dummyIngredient);
            });

            HashMap<String, List<DummyIngredient>> hashMap = new HashMap<>();
            hashMap.put("ingredients", ingredientsList);
            gson.toJson(hashMap, fileWriter);

        } catch (IOException | JsonIOException exception) {
            logger.error("Error while saving test data ", exception);
        }
        logger.info("Done creating file ingredients.json");
    }

    /**
     *
     * @param title Ingredient title name
     * @param bestBeforeDays Parameter value will be used to add to the current day
     * @param useByDays Parameter value will be used to add to the current day
     *
     * @return DummyIngredient object
     */
    private DummyIngredient createDummyIngredient(String title, int bestBeforeDays, int useByDays) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        DummyIngredient dummyIngredient = new DummyIngredient();
        dummyIngredient.setTitle(title);
        dummyIngredient.setBestBefore(dateTimeFormatter.format(now.plusDays(bestBeforeDays)));
        dummyIngredient.setUseBy(dateTimeFormatter.format(now.plusDays(useByDays)));

        return dummyIngredient;
    }

    /**
     *
     * @param fileName
     * @param ingredients
     * @param bestBeforeDays
     * @param useByDays
     */
    public void createUnitTestFile(String fileName, List<String> ingredients, int bestBeforeDays, int useByDays) {
        logger.info("Start populating data for unit test");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(fileName)){
            List<DummyIngredient> ingredientsList = new ArrayList<>();

            ingredients.forEach(ingredient -> {
                DummyIngredient dummyIngredient = createDummyIngredient(ingredient, bestBeforeDays, useByDays);
                ingredientsList.add(dummyIngredient);
            });

            HashMap<String, List<DummyIngredient>> hashMap = new HashMap<>();
            hashMap.put("ingredients", ingredientsList);
            gson.toJson(hashMap, fileWriter);

        } catch (IOException | JsonIOException exception) {
            logger.error("Error while saving test data ", exception);
        }
    }

}
