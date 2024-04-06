package com.ocado.basket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;

public class BasketSplitter {
    private final List<Product> _productsConfig;
    public BasketSplitter(String absolutePathToConfigFile) throws IOException {
        this._productsConfig = JsonLoader.loadConfig(absolutePathToConfigFile);
    }

    /**
     * Splits the provided list of product names into optimized baskets based on delivery options.
     * @param items A list of product names to be split.
     * @return      A mapping of delivery options to lists of product names.
     */
    public Map<String, List<String>> split(List<String> items){
        List<Product> productsToSplit = loadProductsToSplit(items);

        //create hashmap that counts how many items has specific delivery option available
        HashMap<String, Integer> deliveryFrequencyMap = generateFrequencyMap(productsToSplit);

        String mostFrequentDelivery = mapMaxValueKey(deliveryFrequencyMap);
        Map<String, List<String>> outputBasket = new HashMap<>();

        while(mostFrequentDelivery != null && !productsToSplit.isEmpty()){
            String finalCurrentMaxDelivery = mostFrequentDelivery;
            List<String> products = new ArrayList<>();

            //check each element if contains desired delivery option, if so add it to output list, and update hashmap
            for(int i = productsToSplit.size()-1; i >= 0; i--){
                Product currentProduct = productsToSplit.get(i);

                if(!currentProduct.getDeliveryOptions().contains(finalCurrentMaxDelivery)) continue;

                for(String deliveryOption : currentProduct.getDeliveryOptions())
                    deliveryFrequencyMap.replace(deliveryOption, deliveryFrequencyMap.get(deliveryOption)-1);

                products.add(currentProduct.getProductName());
                productsToSplit.remove(currentProduct);
            }

            outputBasket.put(finalCurrentMaxDelivery, products);
            deliveryFrequencyMap.remove(mostFrequentDelivery);
            mostFrequentDelivery = mapMaxValueKey(deliveryFrequencyMap);
        }
        return outputBasket;
    }

    /**
     * Counts how many items have each specific delivery option available.
     * @param productsToSplit A list of products objects which is used to initialize our HashMap.
     * @return                A mapping of delivery options to their occurrence count in the provided products list.
     */
    private HashMap<String, Integer> generateFrequencyMap(List<Product> productsToSplit){
        HashMap<String, Integer> deliveryMap = new HashMap<>();
        for(Product product : productsToSplit){
            List<String> deliveryOptions = product.getDeliveryOptions();
            for(String deliveryOption : deliveryOptions){
                //if there is no product with specific delivery option, meaning this particular delivery option does not exist in hash map, create it with value of 1
                if(!deliveryMap.containsKey(deliveryOption)){
                    deliveryMap.put(deliveryOption, 1);
                }
                //in case there already exist a product with this particular delivery, just increase it by 1
                else {
                    deliveryMap.put(deliveryOption, deliveryMap.get(deliveryOption) + 1);
                }
            }
        }
        return deliveryMap;
    }

    /**
     * Changes list of Strings into a list of Product objects.
     * @param items     A list of Strings with products names.
     * @return          A list of Product objects.
     */
    private List<Product> loadProductsToSplit(List<String> items) {
        List<Product> output = new ArrayList<>();
        for(Product product : _productsConfig)
            if(items.contains(product.getProductName()))
                output.add(product);
        return output;
    }

    private String mapMaxValueKey(Map<String, Integer> hashMap){
        return hashMap.keySet().stream().max(Comparator.comparing(hashMap::get)).orElse(null);
    }
}

