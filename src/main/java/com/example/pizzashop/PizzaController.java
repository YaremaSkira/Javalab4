package com.example.pizzashop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class PizzaController {
    private final AtomicLong counter = new AtomicLong();
    public static Path tempFile;

    static {
        try {
            tempFile = Files.createTempFile("pizza", null);
            Files.write(tempFile, "{}".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/pizzas")
    public HashMap<Long, Pizza> getAllPizzas() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Pizza> pizzas = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Pizza>>() {});

        return pizzas;
    }

    @GetMapping("/pizza/{id}")
    public Pizza getPizza(@PathVariable Long id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Pizza> pizzas = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Pizza>>() {});

        return pizzas.get(id);
    }

    @PostMapping("/pizza")
    public Pizza createPizza(@RequestParam(value = "name") String name) throws IOException {
        Pizza pizza = new Pizza(counter.incrementAndGet(), name);
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Pizza> pizzas = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Pizza>>() {});

        pizzas.put(pizza.getId(), pizza);

        mapper.writeValue(tempFile.toFile(), pizzas);

        return pizza;
    }

    @DeleteMapping("/pizza/{id}")
    public String removePizza(@PathVariable Long id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Pizza> pizzas = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Pizza>>() {});

        if (pizzas.containsKey(id)) {
            pizzas.remove(id);
            mapper.writeValue(tempFile.toFile(), pizzas);

            return "Removed";
        }

        return "Nothing to remove";
    }
}
