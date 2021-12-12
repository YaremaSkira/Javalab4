package com.example.pizzashop;

import java.util.ArrayList;

public class Order {
    private final long id;
    private final long pizzaId;
    private final ArrayList<Long> toppingsIds;


    public Order() {
        this.id = 0;
        this.pizzaId = 0;
        this.toppingsIds = null;
    }

    public Order(long id, Long pizzaId, ArrayList<Long> toppingIds) {
        this.id = id;
        this.pizzaId = pizzaId;
        this.toppingsIds = toppingIds;
    }

    public long getId() {
        return id;
    }


    public Long getPizzaId() {
        return pizzaId;
    }

    public ArrayList<Long> getToppingsIds() {
        return toppingsIds;
    }
}
