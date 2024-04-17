package com.example.foodorder.models;

import java.util.List;
import java.util.UUID;

public class OrderDetail {
    private String orderDetailID;
    private Order order;
    private Food foods;
    private int quantity;

    public OrderDetail(Order order, Food foods, int quantity) {
        this.orderDetailID = UUID.randomUUID().toString();
        this.order = order;
        this.foods = foods;
        this.quantity = quantity;
    }

    public OrderDetail() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Food getFoods() {
        return foods;
    }

    public void setFoods(Food foods) {
        this.foods = foods;
    }
}
