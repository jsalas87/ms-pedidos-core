package com.rfsc.pedidos.adapter.mongo.model;

import com.rfsc.pedidos.domain.Client;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class ClientModel {

    Integer id;
    String rut;
    @Field("razon_social")
    String reason;
    @Field("direccion")
    String address;
    @Field("telefono_principal")
    String phoneNumber;

    public static ClientModel of(Client client) {
        return ClientModel.builder()
                .id(client.getId())
                .rut(client.getRut())
                .reason(client.getReason())
                .address(client.getAddress())
                .phoneNumber(client.getPhoneNumber())
                .build();
    }
}
