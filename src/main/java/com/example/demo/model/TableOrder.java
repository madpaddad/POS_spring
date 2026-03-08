package com.example.demo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class TableOrder {

    @Id
    private String id;

    @Field
    private String table;

    @Field
    private ObjectId order;

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

    public ObjectId getOrder() {
        return order;
    }

    public void setOrder(ObjectId order) {
        this.order = order;
    }

    public TableOrder(){}

    public TableOrder(String table, ObjectId order){
        this.table = table;
        this.order = order;
    }
}
