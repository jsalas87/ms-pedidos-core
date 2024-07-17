package com.rfsc.pedidos.adapter.mongo.model;

import com.rfsc.pedidos.domain.Order;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.stream.Collectors;

@Document("order")
@Data
@Builder
public class OrderModel {

    @Id
    Integer id;
    @Field("cliente")
    ClientModel client;
    @Field("productos")
    List<ProductModel> products;
    @Field("fecha_pedido")
    String date;
    @Field("metodo_pago")
    String payMethod;

    public static OrderModel of(Order order) {
        return OrderModel.builder()
                .id(order.getId())
                .client(ClientModel.of(order.getClient()))
                .products(order.getProducts().stream().map(ProductModel::of).collect(Collectors.toList()))
                .date(order.getDate())
                .payMethod(order.getPayMethod())
                .build();
    }
}
