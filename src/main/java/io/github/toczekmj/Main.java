package io.github.toczekmj;

import com.ocado.basket.BasketSplitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        List<String> items = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Fond - Chocolate", "Cookies - Englishbay Wht");
        List<String> items = Arrays.asList("Steak (300g)", "Carrots (1kg)",
                "Cold Beer (330ml)",
                "AA Battery (4 Pcs.)", "Espresso Machine", "Garden Chair");
        BasketSplitter basketSplitter = new BasketSplitter("/Users/toczekmj/IdeaProjects/ocado/src/main/resources/config.json");
        basketSplitter.split(items);
    }
}