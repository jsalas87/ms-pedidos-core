package com.rfsc.pedidos.application.port.out;

import com.rfsc.pedidos.domain.Product;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProductRepository {

    Flux<Product> getProducts(List<Integer> productsId);
}
