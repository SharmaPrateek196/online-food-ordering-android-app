package com.dto;

import java.io.Serializable;

public class FoodItem implements Serializable
{
    private int id;
    private String itemName;
    private String itemDesc;
    private int itemPrice;
    private int percentoff;
    private int actual_price;
    private String img_path;
    private boolean itemStatus;
    private int personCount;
    private int categoryID;

    public FoodItem() {
    }

    public FoodItem(int id, String itemName, String itemDesc, int itemPrice, String img_path, boolean itemStatus, int personCount, int categoryID,
                    int percentoff,int actual_price) {
        this.id = id;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
        this.img_path = img_path;
        this.itemStatus = itemStatus;
        this.personCount = personCount;
        this.categoryID = categoryID;
        this.percentoff = percentoff;
        this.actual_price = actual_price;
    }

    public int getPercentoff() {
        return percentoff;
    }

    public void setPercentoff(int percentoff) {
        this.percentoff = percentoff;
    }

    public int getActual_price() {
        return actual_price;
    }

    public void setActual_price(int actual_price) {
        this.actual_price = actual_price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public boolean isItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

}
