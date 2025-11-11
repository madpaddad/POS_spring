package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;


@Document("product")
public class Product {

//    @Transient
//    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String category_name;

    @Field
    private double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_name;
    }

    public void setCategory_id(String category_id) {
        this.category_name = category_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category_name;
    }

    public void setCategory(String category) {
        this.category_name = category;
    }

    public Product(){}

    public Product(String id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category_name = category;
        this.price = price;
    }

    public Product(String name, String category, double price) {
//        this.id = id;
        this.name = name;
        this.category_name = category;
        this.price = price;
    }


    public static List<Product> seedProduct() {
        return Arrays.asList(
            new Product("1", "គុយទាវ/មីសាច់គោ", "គុយទាវ/មី", 10000),
            new Product("2", "គុយទាវ/មីប្រហិតបាក់សៀប", "គុយទាវ/មី", 10000),
            new Product("3", "បាយសាច់ជ្រូក", "បាយ", 10000),
            new Product("4", "បាយភ្លៅមាន់ចៀន", "បាយ", 10000)
        );
    }
}
