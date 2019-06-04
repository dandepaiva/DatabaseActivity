package com.example.DatabaseActivity;

public class Product {
    //private int id;
    private String productName;
    private int quantity;

    public Product(){}

    public Product(String name, int quantity) {
        //this.id = id;
        this.productName = name;
        this.quantity = quantity;
    }
/*
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
*/
    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product: " + productName + " : " + quantity;
    }
}
