package com.ocado.basket;

import java.util.List;

class Product {
    private final String productName;
    private final List<String> deliveryOptions;
    public Product(String productName, List<String> deliveryOptions){
        this.productName = productName;
        this.deliveryOptions = deliveryOptions;
    }
    public String getProductName(){
        return this.productName;
    }
    public List<String> getDeliveryOptions(){
        return this.deliveryOptions;
    }
}
