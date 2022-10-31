package com.jobhunt.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Ingredient {

    private String title;
    private LocalDate bestBefore;
    private LocalDate useBy;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(" title:");
        stringBuilder.append(title).append(" bestBefore:").append(bestBefore);
        stringBuilder.append(" useBy:").append(useBy);
        return stringBuilder.toString();
    }
}
