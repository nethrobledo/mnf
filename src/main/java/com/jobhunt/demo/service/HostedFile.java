package com.jobhunt.demo.service;

import com.jobhunt.demo.client.RecipeFileClient;
import com.jobhunt.demo.model.Post;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
public class HostedFile implements FileStrategy {

    @Value("https://api.jsonbin.io/v3/b/63734f7665b57a31e6b8042a")
    private String fileUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RecipeFileClient recipeFileClient;

    @Override
    public ArrayList<LinkedHashMap> getIngredients() {
        logger.info("About to get ingredients " + fileUrl);
        Mono<Post> monoResult = recipeFileClient.doGet(fileUrl, generateHeader());
        return null;
    }

    @Override
    public ArrayList<LinkedHashMap> getRecipes() {
        return null;
    }

    private MultiValueMap<String, String> generateHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        return headers;
    }
}
