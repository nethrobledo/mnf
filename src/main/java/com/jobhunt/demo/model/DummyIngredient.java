package com.jobhunt.demo.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DummyIngredient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String title;

    @SerializedName("best-before")
    private String bestBefore;

    @SerializedName("use-by")
    private String useBy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(String bestBefore) {
        this.bestBefore = bestBefore;
    }

    public String getUseBy() {
        return useBy;
    }

    public void setUseBy(String useBy) {
        this.useBy = useBy;
    }

    public LocalDate getBestBeforeDate() {
        return LocalDate.parse(bestBefore, formatter);
    }

    public LocalDate getUseByDate() {
        return LocalDate.parse(useBy, formatter);
    }
}
