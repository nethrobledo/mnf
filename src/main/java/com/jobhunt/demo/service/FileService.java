package com.jobhunt.demo.service;

import java.util.ArrayList;

import java.util.LinkedHashMap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HostedFile hostedFile;
    private final LocalFile localFile;

    public ArrayList<LinkedHashMap> getRecipes() {
        return localFile.getRecipes();
    }

    public ArrayList<LinkedHashMap> getIngredients() {
        return localFile.getIngredients();
    }

}
