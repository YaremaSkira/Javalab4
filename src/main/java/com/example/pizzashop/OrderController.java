package com.example.pizzashop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class OrderController {
    private final AtomicLong counter = new AtomicLong();
    private Path tempFile;

    public OrderController() {
        try {
            tempFile = Files.createTempFile("order", null);
            Files.write(tempFile, "{}".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/orders")
    public HashMap<Long, Order> getAllOrders() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Order> orders = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Order>>() {});

        return orders;
    }

    @GetMapping("/order/{id}")
    public HashMap<String, Object> getOrder(@PathVariable Long id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Order> orders = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Order>>() {});
        HashMap<Long, Pizza> pizzas = mapper.readValue(PizzaController.tempFile.toFile(), new TypeReference<HashMap<Long, Pizza>>() {});
        HashMap<Long, Topping> toppings = mapper.readValue(ToppingController.tempFile.toFile(), new TypeReference<HashMap<Long, Topping>>() {});

        Order order = orders.get(id);

        ArrayList<Topping> pizzaToppings = new ArrayList<>();
        for (Long t : order.getToppingsIds()) {
            pizzaToppings.add(toppings.get(t));
        }

        HashMap<String, Object> orderView = new HashMap<>();
        orderView.put("id", order.getId());
        orderView.put("pizza", pizzas.get(order.getPizzaId()));
        orderView.put("toppings", pizzaToppings);

        return orderView;
    }

    @PostMapping("/order")
    public Order createOrder(@RequestParam(value = "pizzaId") Long pizzaId,
                             @RequestParam(value = "toppingIds") ArrayList<Long> toppingIds) throws IOException {

        Order order = new Order(counter.incrementAndGet(), pizzaId, toppingIds);
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Order> orders = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Order>>() {});

        orders.put(order.getId(), order);

        mapper.writeValue(tempFile.toFile(), orders);

        return order;
    }

    @DeleteMapping("/order/{id}")
    public String removeOrder(@PathVariable Long id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Order> orders = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Order>>() {});

        if (orders.containsKey(id)) {
            orders.remove(id);
            mapper.writeValue(tempFile.toFile(), orders);

            return "Removed";
        }

        return "Nothing to remove";
    }
}
