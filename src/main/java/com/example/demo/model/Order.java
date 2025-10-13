package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Document("order")
public class Order {

    @Id
    private String id;

    @Field
    private String tableNo;

    @Field
    private OrderStatus orderStatus;

//    @DBRef
    private Collection<String> orderItemsIds;

    public String getId() {
        return id;
    }

    public String getTableNo() {
        return tableNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Collection<String> getOrderItems() {
        return orderItemsIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(Collection<String> orderItems) {
        this.orderItemsIds = orderItems;
    }

    public Order(String id, String tableNo, OrderStatus orderStatus, Collection<String> orderItemsIds) {
        this.id = id;
        this.tableNo = tableNo;
        this.orderStatus = orderStatus;
        this.orderItemsIds = orderItemsIds;
    }

    public static List<Order> seedOrder(){
        return Arrays.asList(
                new Order("1", "2", OrderStatus.completed, Arrays.asList("1", "2")),
                new Order("2", "3", OrderStatus.completed, Arrays.asList("3", "4"))
        );
    }
}
