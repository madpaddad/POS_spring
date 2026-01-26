package com.example.demo.dto.product;

import java.util.Map;

public class UpdateProductDTO {

    private String id;
    private String name;
    private String new_name;
    private String category;
    private Double price;
    private Boolean is_available;
    private boolean has_subproduct;
    private Map<String, Integer> sub_product;

    public UpdateProductDTO(String name, String new_name, String category, Double price, Boolean is_available, boolean has_subproduct, Map<String, Integer> sub_product) {
        this.name = name;
        this.new_name = new_name;
        this.category = category;
        this.price = price;
        this.is_available = is_available;
        this.has_subproduct = has_subproduct;
        this.sub_product = sub_product;
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

    public String getNew_name() {
        return new_name;
    }

    public void setNew_name(String new_name) {
        this.new_name = new_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }

    public boolean isHas_subproduct() {
        return has_subproduct;
    }

    public void setHas_subproduct(boolean has_subproduct) {
        this.has_subproduct = has_subproduct;
    }

    public Map<String, Integer> getSub_product() {
        return sub_product;
    }

    public void setSub_product(Map<String, Integer> sub_product) {
        this.sub_product = sub_product;
    }


}
