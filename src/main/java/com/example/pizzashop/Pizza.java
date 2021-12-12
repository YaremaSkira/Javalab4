package com.example.pizzashop;

public class Pizza {
    private final long id;
    private final String name;

    public Pizza() {
        this.id = 0;
        this.name = "name";
    }

    public Pizza(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
