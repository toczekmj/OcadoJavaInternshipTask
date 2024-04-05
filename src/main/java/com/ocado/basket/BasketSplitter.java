package com.ocado.basket;

import java.util.*;

public class BasketSplitter {
    private final List<Product> _productsConfig;
    public BasketSplitter(String absolutePathToConfigFile){
        this._productsConfig = JsonLoader.loadConfig(absolutePathToConfigFile);
    }
    public Map<String, List<String>> split(List<String> items){
        //first attempts to load products
        List<Product> productsToSplit = loadProductsToSplit(items);

        //now create hashmap that counts how many items has specific delivery option available
        HashMap<String, Integer> deliveryNameByItemCount = divide(productsToSplit);

        //get delivery option with the biggest available number of products, that is as well our biggest basket group at the time
        String currentMaxDelivery = getHashMapMaxValueEntry(deliveryNameByItemCount);
        Map<String, List<String>> outputBasket = new HashMap<>();

        //as long as we have more products - perform further splitting.
        //with sanitized and properly provided data there shouldn't exist a possibility for our currentMaxDelivery to return null,
        //but we can check it here just in case
        while(currentMaxDelivery != null && !productsToSplit.isEmpty()){
            String finalCurrentMaxDelivery = currentMaxDelivery;
            List<String> products = new ArrayList<>();

            //check each element if contains desired delivery option, if so add it to output list,
            //but since this particular element 'disappears' from the list, we have to update our hashmap
            //otherwise we'll get not necessarily true outputs later, therefore we won't have optimal basket
            for(int i = productsToSplit.size()-1; i >= 0; i--){
                Product currentProduct = productsToSplit.get(i);
                if(currentProduct.getDeliveryOptions().contains(finalCurrentMaxDelivery)){
                    for(String deliveryOption : currentProduct.getDeliveryOptions())
                        deliveryNameByItemCount.replace(deliveryOption, deliveryNameByItemCount.get(deliveryOption)-1);
                    products.add(currentProduct.getProductName());
                    productsToSplit.remove(currentProduct);
                }
            }

            outputBasket.put(finalCurrentMaxDelivery, products);
            deliveryNameByItemCount.remove(currentMaxDelivery);
            currentMaxDelivery = getHashMapMaxValueEntry(deliveryNameByItemCount);
        }
        return outputBasket;
    }

    private String getHashMapMaxValueEntry(Map<String, Integer> hashMap){
        return hashMap.keySet().stream().max(Comparator.comparing(hashMap::get)).orElse(null);
    }

    private HashMap<String, Integer> divide(List<Product> productsToSplit){
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

    private List<Product> loadProductsToSplit(List<String> items) {
        List<Product> output = new ArrayList<>();
        for(Product product : _productsConfig)
            if(items.contains(product.getProductName()))
                output.add(product);
        return output;
    }
}

