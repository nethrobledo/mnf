package com.sample.mnf.demo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import com.sample.mnf.demo.model.DummyIngredient;
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
 * Data service with the main function of populating test data
 * Both used by server and unit testing
 *
 * NOTE:It is not clear to me if there is a need for the ingredients and recipes to be dynamic.
 * I have assumed they are static except for the best-before and use-by dates.
 */
@Service
public class DummyDataService {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final List<String> meatIngredients = new ArrayList<>();
    private static final List<String> saladIngredients = new ArrayList<>();
    private static final List<String> otherIngredients = new ArrayList<>();

    static {
        meatIngredients.add("Ham");
        meatIngredients.add("Bacon");
        meatIngredients.add("Sausage");

        saladIngredients.add("Mushroom");
        saladIngredients.add("Lettuce");
        saladIngredients.add("Tomato");
        saladIngredients.add("Cucumber");
        saladIngredients.add("Beetroot");
        saladIngredients.add("Spinach");
        saladIngredients.add("Salad Dressing");

        otherIngredients.add("Eggs");
        otherIngredients.add("Cheese");
        otherIngredients.add("Bread");
        otherIngredients.add("Butter");
        otherIngredients.add("Hotdog Bun");
        otherIngredients.add("Ketchup");
        otherIngredients.add("Mustard");
        otherIngredients.add("Milk");
        otherIngredients.add("Salt");
        otherIngredients.add("Pepper");
    }
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
            DummyIngredient dummyIngredient;

            for (String ingredient:meatIngredients) {
                dummyIngredient = createDummyIngredient(ingredient, -3, -3);
                ingredientsList.add(dummyIngredient);
            }

            for (String ingredient:saladIngredients) {
                dummyIngredient = createDummyIngredient(ingredient, 0, 0);
                ingredientsList.add(dummyIngredient);
            }

            for (String ingredient:otherIngredients) {
                dummyIngredient = createDummyIngredient(ingredient, 10, 10);
                ingredientsList.add(dummyIngredient);
            }

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
            for (String ingredient:ingredients) {
                DummyIngredient dummyIngredient = createDummyIngredient(ingredient, bestBeforeDays, useByDays);
                ingredientsList.add(dummyIngredient);
            }
            HashMap<String, List<DummyIngredient>> hashMap = new HashMap<>();
            hashMap.put("ingredients", ingredientsList);
            gson.toJson(hashMap, fileWriter);

        } catch (IOException | JsonIOException exception) {
            logger.error("Error while saving test data ", exception);
        }
    }

}
