package com.jobhunt.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobhunt.demo.client.RecipeFileClient;
import com.jobhunt.demo.model.Post;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class FileService {

    @Value("${sample.baseUrl}")
    private String baseUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String SAMPLE_URI = "/posts/1";
    private static final ObjectMapper mapper = new ObjectMapper();
    // TODO:: To be converted to @Value property
    private String fileName;
    private final RecipeFileClient recipeFileClient;

    @Autowired
    public FileService(RecipeFileClient recipeFileClient) {
        this.recipeFileClient = recipeFileClient;
    }

    // In the future when we want to retrieve file from another site
    public Mono<Post> getRecipesFromSite() {
        return recipeFileClient.doGet(baseUrl + SAMPLE_URI, generateHeader());
    }

    public ArrayList<LinkedHashMap> getRecipesFromLocal() {
        ArrayList<LinkedHashMap> recipes = null;

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/recipes.json")) {
            LinkedHashMap linkedHashMap = mapper.readValue(inputStream, LinkedHashMap.class);
            recipes = (ArrayList) linkedHashMap.get("recipes");
        } catch (IOException e) {
            logger.error("Error while parsing " + e.getMessage());
        }
        return recipes;
    }

    public ArrayList<LinkedHashMap> getIngredientsFromLocal() {
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    String getFileName() {
        return this.fileName;
    }

    private MultiValueMap<String, String> generateHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        return headers;
    }

}
