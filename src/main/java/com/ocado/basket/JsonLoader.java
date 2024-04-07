package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class JsonLoader {
    public static List<Product> loadConfig(String JSONPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Product> config = new ArrayList<>();
        File from = new File(JSONPath);
        TypeReference<LinkedHashMap<String,List<String>>> typeRef = new TypeReference<>() {};

        mapper.readValue(from, typeRef).forEach((name, deliveryList) ->
            config.add(new Product(name, deliveryList))
        );

        return config;
    }
}
