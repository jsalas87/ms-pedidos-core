package com.rfsc.pedidos.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {

    private Integer id;
    private String rut;
    private String reason;
    private String address;
    private String phoneNumber;
}
