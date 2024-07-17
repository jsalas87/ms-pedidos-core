package com.rfsc.pedidos.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Product {

    Integer id;
    Integer quantity;
    Integer price;
    String description;
    String brand;
    String dateLastInventory;
}
