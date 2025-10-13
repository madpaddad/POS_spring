package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("order_item")
public class OrderItem {

    @Id
    private String id;

    @Field
    private int product_id;

    @Field
    private int quantity;

    @Field
    private String description;

    @Field
    private double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderItem(String id, int product_id, int quantity, String description, double price) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
    }

    public OrderItem(String id, int product_id, int quantity, double price) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
    }
}
