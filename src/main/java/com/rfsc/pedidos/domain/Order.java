package com.rfsc.pedidos.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
public class Order {

    Integer id;
    @With
    Client client;
    @With
    List<Product> products;
    String date;
    String payMethod;
}
