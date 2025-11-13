package com.example.demo.dto.order;


//import org.springframework.data.mongodb.core.mapping.Document;

//@Document
public class ProductDTO {
    private String id;
    private String name;
//    private String category_id;
    private String category;
    private Double price;

    public ProductDTO(String name, String category, Double price){
        this.name = name;
        this.category = category;
        this.price = price;
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
}