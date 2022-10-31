package com.jobhunt.demo.model;

import com.google.gson.annotations.SerializedName;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Setter
public class DummyIngredient {
    
    private String title;

    @SerializedName("best-before")
    private String bestBefore;

    @SerializedName("use-by")
    private String useBy;

}
