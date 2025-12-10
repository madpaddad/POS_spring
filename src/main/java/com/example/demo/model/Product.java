package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

@Document("product")
public class Product {

//    @Transient
//    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String category;

    @Field
    private double price;

    @Field
    private boolean has_subproduct;

    @Field
    private String path;

    @Field
    private Map<String, Integer> sub_product = new HashMap<>(); 


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category;
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
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Map<String, Integer> getsub_product() {
        return sub_product;
    }

    public void setsub_product(Map<String, Integer> sub_product) {
        this.sub_product = sub_product;
    }

    public Product(){}

    public boolean isHas_subproduct() {
        return has_subproduct;
    }

    public void setHas_subproduct(boolean has_subproduct) {
        this.has_subproduct = has_subproduct;
    }

    public Product(String id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.has_subproduct = false;
    }


    public Product(String name, String category, Map<String, Integer> sub_product){
        this.name = name;
        this.category = category;
        this.sub_product = sub_product;
        this.has_subproduct = true;
    }


   
public static List<Product> seedProduct() {


    return Arrays.asList(
        new Product("1", "គុយទាវ/មីសាច់គោ", "គុយទាវ/មី", 10000),
        new Product("2", "គុយទាវ/មីប្រហិតបាក់សៀប", "គុយទាវ/មី", 10000),
        new Product("3", "បាយសាច់ជ្រូក", "បាយ", 10000),
        new Product("4", "បាយភ្លៅមាន់ចៀន", "បាយ", 10000),
            new Product(
                    "គុយទាវពិសេស",
                    "បាយ",
                    Map.of(
                            "ចានតូច", 2000,
                            "ចានធំ", 4000
                    )
            )
    );
    }
}
