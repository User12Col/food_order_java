package com.example.foodorder.models;

public class ItemSelect {
    private String title;
    private int image;

    public ItemSelect(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public ItemSelect() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
