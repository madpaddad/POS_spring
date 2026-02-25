package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class TableOrder {

    @Id
    private String id;

    @Field
    private String table;

    @Field
    private String order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public TableOrder(){}

    public TableOrder(String table, String order){
        this.table = table;
        this.order = order;
    }
}
