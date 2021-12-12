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
public class ToppingController {
    private final AtomicLong counter = new AtomicLong();
    public static Path tempFile;

    static {
        try {
            tempFile = Files.createTempFile("topping", null);
            Files.write(tempFile, "{}".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/toppings")
    public HashMap<Long, Topping> getAllToppings() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Topping> toppings = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Topping>>() {});

        return toppings;
    }

    @GetMapping("/topping/{id}")
    public Topping getTopping(@PathVariable Long id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Topping> toppings = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Topping>>() {});

        return toppings.get(id);
    }

    @PostMapping("/topping")
    public Topping createTopping(@RequestParam(value = "name") String name) throws IOException {
        Topping topping = new Topping(counter.incrementAndGet(), name);
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Topping> toppings = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Topping>>() {});

        toppings.put(topping.getId(), topping);

        mapper.writeValue(tempFile.toFile(), toppings);

        return topping;
    }

    @DeleteMapping("/topping/{id}")
    public String removeTopping(@PathVariable Long id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Long, Topping> toppings = mapper.readValue(tempFile.toFile(), new TypeReference<HashMap<Long, Topping>>() {});

        if (toppings.containsKey(id)) {
            toppings.remove(id);
            mapper.writeValue(tempFile.toFile(), toppings);

            return "Removed";
        }

        return "Nothing to remove";
    }
}
