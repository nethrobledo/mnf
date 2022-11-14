package com.jobhunt.demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface FileStrategy {
    ArrayList<LinkedHashMap> getIngredients();
    ArrayList<LinkedHashMap> getRecipes();
}
