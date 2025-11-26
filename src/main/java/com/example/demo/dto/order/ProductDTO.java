package com.example.demo.dto.order;

import com.example.demo.model.Product;

import java.util.Map;

public class ProductDTO {
    private String id;
    private String name;
    private String category;
    private Double price;
    private boolean has_subproduct;
    private Map<String, Integer> sub_product;

    public ProductDTO() {
    }

    // Constructor for normal product (no sub-products)
    public ProductDTO(String name, String category, Double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.has_subproduct = false;
    }

    // Constructor for product WITH sub-products
    public ProductDTO(String name, String category, Map<String, Integer> sub_product) {
        this.name = name;
        this.category = category;
        this.sub_product = sub_product;
        this.has_subproduct = true;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean ishas_subproducts() {
        return has_subproduct;
    }

    public void sethas_subproducts(boolean has_subproducts) {
        this.has_subproduct = has_subproducts;
    }

    public Map<String, Integer> getSub_products() {
        return sub_product;
    }

    public void setSub_products(Map<String, Integer> sub_products) {
        this.sub_product = sub_products;
    }
}
