package com.example.foodorder.models;

import java.util.UUID;

public class Order {
    private String orderID;
    private String date;
    private String status;
    private String address;
    private double totalPrice;
    private User user;
    private User employee;

    public Order() {
    }

    public Order(String orderID, String date, String status, String address, double totalPrice, User user, User employee) {
        this.orderID = orderID;
        this.date = date;
        this.status = status;
        this.address = address;
        this.totalPrice = totalPrice;
        this.user = user;
        this.employee = employee;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
