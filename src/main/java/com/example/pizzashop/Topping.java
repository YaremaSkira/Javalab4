package com.example.pizzashop;

public class Topping {
    private final long id;
    private final String name;

    public Topping() {
        this.id = 0;
        this.name = "name";
    }

    public Topping(long id, String name) {
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
