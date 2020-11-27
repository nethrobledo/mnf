package com.jobhunt.demo.model;

import java.time.LocalDate;

public class Ingredient {

    private String title;
    private LocalDate bestBefore;
    private LocalDate useBy;

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(LocalDate bestBefore) {
        this.bestBefore = bestBefore;
    }

    public LocalDate getUseBy() {
        return useBy;
    }

    public void setUseBy(LocalDate useBy) {
        this.useBy = useBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(" title:");
        stringBuilder.append(title).append(" bestBefore:").append(bestBefore);
        stringBuilder.append(" useBy:").append(useBy);
        return stringBuilder.toString();
    }
}
