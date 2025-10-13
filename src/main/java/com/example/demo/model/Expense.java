package com.example.demo.model;

import org.checkerframework.common.value.qual.EnumVal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("expense")
public class Expense {
    @Id
    private String id;

    @Field(name="name")
    @Indexed(unique=true)
    private String expanseName;

    @Field(name = "category")
    private ExpenseCategory expenseCategory;

    @Field(name = "amount")
    private Number quantity;

    public Number getQuantity() {
        return quantity;
    }

    public void setQuantity(Number quantity) {
        this.quantity = quantity;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpanseName() {
        return expanseName;
    }

    public void setExpanseName(String expanseName) {
        this.expanseName = expanseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Expense(Number quantity, ExpenseCategory expenseCategory, String expanseName, String id) {
        this.quantity = quantity;
        this.expenseCategory = expenseCategory;
        this.expanseName = expanseName;
        this.id = id;
    }
}
