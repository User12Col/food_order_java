package com.example.foodorder.models;

public class Food {
    private String foodID;
    private String name;
    private double unitPrice;
    private String description;
    private String image;
    private Category category;
    private int isDelete;

    public Food() {
    }

    public Food(String foodID, String name, double unitPrice, String description, String image, Category category, int isDelete) {
        this.foodID = foodID;
        this.name = name;
        this.unitPrice = unitPrice;
        this.description = description;
        this.image = image;
        this.category = category;
        this.isDelete = 0;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
