package com.rfsc.pedidos.application.port.out;

import com.rfsc.pedidos.domain.Client;
import reactor.core.publisher.Mono;

public interface ClientRepository {

    Mono<Client> getClient(Integer clientId);
}
