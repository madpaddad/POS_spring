package com.example.demo.dto.file;

public class UpdateFileDTO {
    private String name;
    private String new_name;


    public UpdateFileDTO(String name, String new_name) {
        this.name = name;
        this.new_name = new_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNew_name() {
        return new_name;
    }

    public void setNew_name(String new_name) {
        this.new_name = new_name;
    }
}
