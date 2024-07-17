package com.rfsc.pedidos.adapter.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfsc.pedidos.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProductKafkaModel {

    @JsonProperty("id_producto")
    Integer productId;
    @JsonProperty("cantidad")
    Integer quantity;

    public Product toDomain() {
        return Product.builder()
                .id(productId)
                .quantity(quantity)
                .build();
    }
}
