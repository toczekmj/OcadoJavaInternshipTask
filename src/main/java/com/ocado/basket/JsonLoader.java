package com.ocado.basket;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class JsonLoader {
    public static List<Product> loadConfig(String JSONPath) throws IOException {
        List<Product> config = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<String>> productMap = objectMapper.readValue(new File(JSONPath), Map.class);
        for(Map.Entry<String, List<String>> entry : productMap.entrySet())
            config.add(new Product(entry.getKey(), entry.getValue()));
        return config;
    }
}
