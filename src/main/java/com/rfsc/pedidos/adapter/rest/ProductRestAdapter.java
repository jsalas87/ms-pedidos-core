package com.rfsc.pedidos.adapter.rest;

import com.rfsc.pedidos.adapter.rest.model.ProductModel;
import com.rfsc.pedidos.application.port.out.ProductRepository;
import com.rfsc.pedidos.config.RestProperties;
import com.rfsc.pedidos.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ProductRestAdapter implements ProductRepository {

    private final RestProperties restProperties;

    public ProductRestAdapter( RestProperties restProperties) {
        this.restProperties = restProperties;
    }

    @Override
    public Flux<Product> getProducts(List<Integer> productIds) {
        log.info("Product Request: {}", restProperties.getProduct());
        String ids = productIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String uri = restProperties.getProduct().replace("{ids}", ids);

        return WebClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(ProductModel.class)
                .map(ProductModel::toDomain)
                .doOnNext(product -> log.info("Llamado a Api Go Productos ha terminado, {}", product));
    }

}
