package com.rfsc.pedidos.adapter.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfsc.pedidos.domain.Client;
import com.rfsc.pedidos.domain.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderKafkaModel {

    @JsonProperty("id")
    private Integer orderId;
    @JsonProperty("id_cliente")
    private Integer clientId;
    @JsonProperty("detalle_pedido")
    private List<OrderProductKafkaModel> detailProduct;
    @JsonProperty("fecha")
    private String date;
    @JsonProperty("metodo_pago")
    private String payMethod;

    public Order toDomain() {

        return Order.builder()
            .id(orderId)
            .client(Client.builder().id(clientId).build())
            .products(detailProduct.stream().map(OrderProductKafkaModel::toDomain).collect(Collectors.toList()))
            .date(date)
            .payMethod(payMethod)
            .build();
    }
}
