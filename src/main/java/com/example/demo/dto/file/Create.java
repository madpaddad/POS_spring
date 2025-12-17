package com.example.demo.dto.file;

public class Create {

    private String category;
    private String name;
//    private File file;

    public String getCategory() {
        return category;
    }

    public String getName(){
        return name;
    }

    public Create(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
