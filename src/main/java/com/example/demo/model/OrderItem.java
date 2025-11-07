package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document("order_items")
public class OrderItem {

    @Id
    private String id;

    @Field
    private String product_id;

    @Field
    private int quantity;

    @Field
    private String description;

    @Field
    private double total;

//    @Transient
    private List<Product> product;


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Product> getProducts() {
        return product;
    }

    public void setProducts(List<Product> product) {
        this.product = product;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
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
        return total;
    }

    public void setPrice(double total) {
        this.total = total;
    }

//    public OrderItem(String id, int product_id, int quantity, String description, double total) {
//        this.id = id;
//        this.product_id = product_id;
//        this.quantity = quantity;
//        this.description = description;
//        this.total = total;
//    }

    public OrderItem(){

    }

    public OrderItem(String id, String product_id, int quantity, double total) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.total = total;
    }

    public static List<OrderItem> seedOrder() {
        return Arrays.asList(
                new OrderItem("1", "2", 2, 20000),
                new OrderItem("2", "3", 4, 40000)
        );
    }
}
