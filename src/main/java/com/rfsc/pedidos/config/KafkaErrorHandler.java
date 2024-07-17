package com.rfsc.pedidos.config;

import com.rfsc.pedidos.application.port.in.ConsumeMessageOrderKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class KafkaErrorHandler implements CommonErrorHandler {

    private static final int MAX_ATTEMPTS = 3;
    private Map<String, Integer> attemptMap = new HashMap<>();

    @Autowired
    private ConsumeMessageOrderKafka consumeMessageOrderKafka;


    @Override
    public void handleRecord(Exception thrownException, ConsumerRecord<?, ?> data, Consumer<?, ?> consumer, MessageListenerContainer container) {

        String key = data.topic() + "-" + data.partition() + "-" + data.offset();
        int attempts = attemptMap.getOrDefault(key, 0);

        if (attempts < MAX_ATTEMPTS) {
            attempts++;
            attemptMap.put(key, attempts);
            log.info("Intento [{}] de [{}] para el mensaje en topic {}-{} con offset {}",
                    attempts, MAX_ATTEMPTS, data.topic(), data.partition(), data.offset());
            try {
                consumer.seek(new TopicPartition(data.topic(), data.partition()), data.offset());
            }catch(Exception e) {
                handleRecord(e, data, consumer, container);
            }
        }else {

            log.info("Se maneja error [{}] encontrado en topic {}-{} con offset {}",
                    thrownException.getMessage(), data.topic(), data.partition(), data.offset());

            TopicPartition topicPartition = new TopicPartition(data.topic(), data.partition());
            log.warn("Se saltea el topic {}-{} con offset {}", data.topic(), data.partition(), data.offset());
            consumer.seek(topicPartition, data.offset() + 1L);
            attemptMap.remove(key);
        }
    }

    @Override
    public void handleRemaining(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
                       MessageListenerContainer container) {
        try {
            log.info("Se maneja error encontrado consumiendo multiples registros");
            log.error("Se encontro error: {}", thrownException.getMessage());

            final String s = thrownException.getMessage()
                    .split("Error deserializing key/value for partition ")[1]
                    .split(". If needed, please seek past the record to continue consumption.")[0];

            final String topic = s.split("-")[0];
            final long offset = Long.parseLong(s.split("offset ")[1]);
            final int partition = Integer.parseInt(s.split("-")[1].split(" at")[0]);

            TopicPartition topicPartition = new TopicPartition(topic, partition);
            log.warn("Se saltea el topic {}-{} con offset {}", topic, partition, offset);
            consumer.seek(topicPartition, offset + 1L);
        } catch (Exception e) {
            log.error("Error inesperado manejando excepcion", e);
            log.error("Excepcion que desencadeno el evento", thrownException);
            throw e;
        }
    }
}
