package com.example.demo.dto.order;

import com.example.demo.model.OrderStatus;

public class UpdateOrderStatus {
    OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UpdateOrderStatus(OrderStatus status) {
        this.status = status;
    }

}
