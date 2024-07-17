package com.rfsc.pedidos.adapter.mongo.model;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderMongoRepository extends ReactiveMongoRepository<OrderModel, Integer> {
}
