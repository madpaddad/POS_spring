package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;


@Document("product")
public class Product {
    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String category_id;

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

    public String getCategory() {
        return category_id;
    }

    public void setCategory(String category) {
        this.category_id = category;
    }

    public Product(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category_id = category;
    }

    public static List<Product> seedProducts() {
        return Arrays.asList(

        );
    }
}
