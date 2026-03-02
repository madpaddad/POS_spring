package com.example.demo.dto.file;

public class UpdateFileDTO {
    private String name;
    private String category;
    private String path;

    public UpdateFileDTO(){}
    public UpdateFileDTO(String name, String category, String path) {
        this.name = name;
        this.category = category;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String new_name) {
        this.category = new_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
