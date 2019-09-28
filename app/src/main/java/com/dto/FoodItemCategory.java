package com.dto;

import java.io.Serializable;

public class FoodItemCategory implements Serializable
{
    private int id;
    private String categoryName;

    public FoodItemCategory() {
    }

    public FoodItemCategory(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
