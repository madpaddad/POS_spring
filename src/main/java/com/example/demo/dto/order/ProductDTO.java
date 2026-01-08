package com.example.demo.dto.order;

import com.example.demo.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public class ProductDTO {
    private String id;
    private String name;
    private String category;
    private Double price;
    private Boolean is_available;
    private boolean has_subproduct;
    private Map<String, Integer> sub_product;
//    private MultipartFile image;


    public ProductDTO() {
    }

    // Constructor for normal product (no sub-products)
    public ProductDTO(String name, Boolean is_available, String category, Double price) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.is_available  = is_available;
        this.has_subproduct = false;
    }

    // Constructor for product WITH sub-products
    public ProductDTO(String name, Boolean is_available, String category, Map<String, Integer> sub_product) {
        this.name = name;
        this.category = category;
        this.is_available = is_available;
        this.sub_product = sub_product;
        this.has_subproduct = true;
    }

    // Updating Product
//    public ProductDTO(Boolean is_available)

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

    public boolean isHas_subproduct() {
        return has_subproduct;
    }

    public void setHas_subproduct(boolean has_subproduct) {
        this.has_subproduct = has_subproduct;
    }

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }


}
