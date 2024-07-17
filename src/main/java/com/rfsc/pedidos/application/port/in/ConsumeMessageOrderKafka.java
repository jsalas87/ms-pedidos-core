package com.rfsc.pedidos.application.port.in;

import reactor.core.publisher.Mono;

public interface ConsumeMessageOrderKafka {

    void listen(String message);
}
