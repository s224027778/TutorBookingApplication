package com.example.cc;

import java.io.Serializable;

public class Module implements Serializable {
    private int id;
    private int categoryId;
    private String name;

    public Module(int id, int categoryId, String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}