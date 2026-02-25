package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;

public class Table {
    @Id
    private String id;

    @Field
    private String table_no;

    @Field
    private TableStatus tableStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public TableStatus getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

    public Table(){

    }

    public Table(TableStatus tableStatus, String table_no, String id) {
        this.tableStatus = tableStatus;
        this.table_no = table_no;
        this.id = id;
    }

    public static List<Table> seedTable() {
        return Arrays.asList(
                new Table(TableStatus.NEW, "1", "1"),
                new Table(TableStatus.NEW, "2", "2"),
                new Table(TableStatus.NEW, "3", "3"),
                new Table(TableStatus.NEW, "4", "4"),
                new Table(TableStatus.NEW, "5", "5"),
                new Table(TableStatus.NEW, "6", "6"),
                new Table(TableStatus.NEW, "7", "7"),
                new Table(TableStatus.NEW, "8", "8"),
                new Table(TableStatus.NEW, "9", "9")
        );
    }
}
