package com.rfsc.pedidos.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfsc.pedidos.adapter.kafka.exception.ReadMessageException;
import com.rfsc.pedidos.adapter.kafka.model.OrderKafkaMessage;
import com.rfsc.pedidos.adapter.kafka.model.OrderKafkaModel;
import com.rfsc.pedidos.adapter.kafka.model.OrderProductKafkaModel;
import com.rfsc.pedidos.adapter.mongo.model.OrderModel;
import com.rfsc.pedidos.application.port.in.ConsumeMessageOrderKafka;
import com.rfsc.pedidos.application.port.out.ClientRepository;
import com.rfsc.pedidos.adapter.mongo.model.OrderMongoRepository;
import com.rfsc.pedidos.application.port.out.ProductRepository;
import com.rfsc.pedidos.config.ErrorCode;
import com.rfsc.pedidos.domain.Client;
import com.rfsc.pedidos.domain.Order;
import com.rfsc.pedidos.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ConsumeMessageOrderKafkaAdapter implements ConsumeMessageOrderKafka {

    private static final String ORDER_KAFKA_TOPIC = "#{'${orders.kafka.topic}'}";
    private static final String ORDER_KAFKA_GROUP_ID = "#{'${orders.kafka.groupId}'}";
    private static final String ORDER_KAFKA_FACTORY = "kafkaListenerContainerFactory";
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMongoRepository orderMongoRepository;


    @Autowired
    public ConsumeMessageOrderKafkaAdapter(
            ClientRepository clientRepository, ProductRepository productRepository, OrderMongoRepository orderMongoRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderMongoRepository = orderMongoRepository;
    }

    @KafkaListener(
            topics = ORDER_KAFKA_TOPIC,
            groupId = ORDER_KAFKA_GROUP_ID,
            containerFactory = ORDER_KAFKA_FACTORY
    )
    public void listen(String message) {
        log.info("Se recibe mensaje de kafka: {}", message);

        OrderKafkaMessage kafkaMessage = readMessage(message);

        if(kafkaMessage.getData()!= null && kafkaMessage.getData().getOrderId()!=null) {
            this.execute(kafkaMessage);
        } else {
            this.executeError(kafkaMessage);
        }


    }

    private OrderKafkaMessage readMessage(String message) {
        try {
            return new ObjectMapper().readValue(message, OrderKafkaMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Error mappeando el mensaje: {}", message, e);
            throw new ReadMessageException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private void execute(OrderKafkaMessage kafkaMessage) {
        log.info("Se consultan datos adicionales para el cliente del mensaje: {}", kafkaMessage.getId());

            Mono<Client> clientMono = clientRepository.getClient(kafkaMessage.getData().getClientId())
                    .doOnNext(client -> log.info("Consulta del cliente {} realizada con éxito", kafkaMessage.getData().getClientId()));

            Flux<Product> productsFlux = productRepository.getProducts(
                            kafkaMessage.getData().getDetailProduct().stream().map(OrderProductKafkaModel::getProductId).collect(Collectors.toList()))
                    .doOnNext(product -> log.info("Consulta de los productos {} realizada con éxito", kafkaMessage.getData().getDetailProduct()));

            clientMono.zipWith(productsFlux.collectList(), (client, products) -> buildOrder(client, products, kafkaMessage.getData()))
                    .flatMap(order -> {
                        log.info("Se va a guardar en BD la orden con id: {}", kafkaMessage.getData().getOrderId());
                        return orderMongoRepository.save(OrderModel.of(order));
                    })
                    .subscribe(
                            success -> log.info("Orden guardada con éxito"),
                            error -> {
                                log.error("Error guardando la orden", error);
                                throw new ReadMessageException(ErrorCode.INTERNAL_ERROR);
                            }
                    );

    }

    private Order buildOrder(Client client, List<Product> products, OrderKafkaModel kafkaOrder) {

        return kafkaOrder.toDomain().withClient(client).withProducts(products);

    }

    private void executeError(OrderKafkaMessage kafkaMessage) {
            throw new ReadMessageException(ErrorCode.INTERNAL_ERROR);
    }

}
