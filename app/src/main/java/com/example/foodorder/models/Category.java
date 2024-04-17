package com.example.foodorder.models;

public class Category {
    private int cateID;
    private String name;
    private int isDelete;

    public Category(int cateID, String name, int isDelete) {
        this.cateID = cateID;
        this.name = name;
        this.isDelete = 0;
    }

    public Category() {
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
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
