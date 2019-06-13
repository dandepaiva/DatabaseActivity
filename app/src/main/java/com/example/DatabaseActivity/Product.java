package com.example.DatabaseActivity;

import android.support.annotation.NonNull;

/**
 * Object containing a String and an Integer
 * supposed to represent the name of the product
 * and respective quantity
 *
 * (Name is being used like an ID
 * the database only allows one Product for each name)
 * <p></p>
 * Could be used to represent stock or a shopping list, for example
 */
public class Product {
    private String productName;
    private int quantity;

    public Product(){}

    public Product(String name, int quantity) {
        this.productName = name;
        this.quantity = quantity;
    }

    /**
     * getter for product name
     * @return {@link String} representing product name, can be empty or null
     */
    public String getProductName() {
        return productName;
    }

    /**
     * setter for product name
     * @param name The name value
     */
    public void setProductName(String name) {
        this.productName = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public String toString() {
        return "Product: " + productName + " : " + quantity;
    }
}
