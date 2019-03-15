package com.example.lynford.android_ecommerce_project.model;

public class Cart {

    private String id, name, description, image, sku;
    private int price,quantity;


    public Cart(){

    }

    public Cart(String id, String name, String description, String image, String sku, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public void compute(int quantity) {

        quantity = quantity*price;
    }

}
