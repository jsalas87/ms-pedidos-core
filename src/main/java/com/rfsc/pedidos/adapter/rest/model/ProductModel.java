package com.rfsc.pedidos.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rfsc.pedidos.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class ProductModel {

    Integer id;
    @JsonProperty("cantidad")
    Integer quantity;
    @JsonProperty("precio")
    Integer price;
    @JsonProperty("descripcion")
    String description;
    @JsonProperty("marca")
    String brand;
    @JsonProperty("fecha_ultimo_inventario")
    String dateLastInventory;

    public Product toDomain() {
        return Product.builder()
                .id(id)
                .quantity(quantity)
                .price(price)
                .description(description)
                .brand(brand)
                .dateLastInventory(dateLastInventory)
                .build();
    }
}
