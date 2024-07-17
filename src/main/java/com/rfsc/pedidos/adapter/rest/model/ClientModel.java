package com.rfsc.pedidos.adapter.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rfsc.pedidos.domain.Client;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientModel {

    String rut;
    @JsonProperty("razon_social")
    String reason;
    @JsonProperty("direccion")
    String address;
    @JsonProperty("telefono_principal")
    String phoneNumber;

    public Client toDomain(Integer id){
        return Client.builder()
                .id(id)
                .rut(rut)
                .reason(reason)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
