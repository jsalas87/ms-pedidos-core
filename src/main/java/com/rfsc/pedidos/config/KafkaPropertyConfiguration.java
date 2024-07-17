package com.rfsc.pedidos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "orders.kafka")
public class KafkaPropertyConfiguration {

    private String address;
    private String groupId;
}
