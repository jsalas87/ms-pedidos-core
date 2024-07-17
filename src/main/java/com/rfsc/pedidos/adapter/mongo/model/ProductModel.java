package com.rfsc.pedidos.adapter.mongo.model;

import com.rfsc.pedidos.domain.Product;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class ProductModel {

    Integer id;
    @Field("cantidad")
    Integer quantity;
    @Field("precio")
    Integer price;
    @Field("descripcion")
    String description;
    @Field("marca")
    String brand;
    @Field("fecha_ultimo_inventario")
    String dateLastInventory;

    public static ProductModel of(Product product) {
        return ProductModel.builder()
                .id(product.getId())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .description(product.getDescription())
                .brand(product.getBrand())
                .dateLastInventory(product.getDateLastInventory())
                .build();
    }
}
