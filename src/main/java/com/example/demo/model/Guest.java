package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("guest")
public class Guest {
    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String phone;
}
