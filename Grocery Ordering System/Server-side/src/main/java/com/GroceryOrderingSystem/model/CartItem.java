package com.GroceryOrderingSystem.model;

public class CartItem {

    private String name;
    private int quantity;
    private String email;

    public CartItem() {

    }

    public CartItem(String name, int quantity, String email) {
        this.name = name;
        this.quantity = quantity;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", email='" + email + '\'' +
                '}';
    }

}
