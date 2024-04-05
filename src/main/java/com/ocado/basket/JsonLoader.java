package com.ocado.basket;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonLoader {
    public static List<Product> loadConfig(String JSONPath)  {
        List<Product> config = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, List<String>> productMap = objectMapper.readValue(new File(JSONPath), Map.class);
            for(Map.Entry<String, List<String>> entry : productMap.entrySet())
                config.add(new Product(entry.getKey(), entry.getValue()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}
