package com.example.phoenix.b2cuseraccess.models;

/**
 * Created by Phoenix on 13-Aug-17.
 */

public class Product {
    private String name;
    private int imageResId;
    private double price, discount;

    public Product(String name, int imageResId, double price, double discount) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.discount = discount;
    }

    public Product(String name, int imageResId, double price) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
