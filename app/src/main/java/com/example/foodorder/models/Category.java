package com.example.foodorder.models;

public class Category {
    private int cateID;
    private String name;

    public Category(int cateID, String name) {
        this.cateID = cateID;
        this.name = name;
    }

    public Category() {
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
