package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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

//    @Transient
    private List<OrderItem> orderItems;


    public String getId() {
        return id;
    }

    public String getTableNo() {
        return tableNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Collection<String> getOrderItemsIds() {
        return orderItemsIds;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderItemsIds(Collection<String> orderItemsIds) {
        this.orderItemsIds = orderItemsIds;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(){

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
