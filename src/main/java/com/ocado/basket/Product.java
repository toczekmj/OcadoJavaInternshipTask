package com.ocado.basket;

import java.util.List;
record Product(String productName, List<String> deliveryOptions) {
}
