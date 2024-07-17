package com.rfsc.pedidos.adapter.kafka;

import com.rfsc.pedidos.adapter.mongo.model.OrderModel;
import com.rfsc.pedidos.application.port.out.ClientRepository;
import com.rfsc.pedidos.adapter.mongo.model.OrderMongoRepository;
import com.rfsc.pedidos.application.port.out.ProductRepository;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.time.Duration;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = ConsumerMessageOrderKafkaAdapterTest.ORDER_KAFKA_TOPIC)
@DirtiesContext
@AutoConfigureDataMongo
public class ConsumerMessageOrderKafkaAdapterTest {

    static final String ORDER_KAFKA_TOPIC = "ecommerce.sales.orders.json_1";

    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;

    @ClassRule
    public static EmbeddedKafkaRule kafkaEmbedded = new EmbeddedKafkaRule(1, true, ORDER_KAFKA_TOPIC);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderMongoRepository orderMongoRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;


    @Before
    public void setUp() throws Exception {
        String bindIp = "localhost";
        int port = 27017;

        MongodStarter starter = MongodStarter.getDefaultInstance();
        ImmutableMongodConfig mongodConfig = ImmutableMongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();

        mongodExecutable = starter.prepare(mongodConfig);
        mongodProcess = mongodExecutable.start();
    }

    @After
    public void tearDown() throws Exception {
        if (mongodProcess != null) {
            mongodProcess.stop();
            mongodExecutable.stop();
        }
    }

    @Test
    public void testListenAndProcessMessage() throws Exception {

        var file = new ClassPathResource("kafka/message_success.json").getFile();
        var fr = new FileReader(file);
        var message = FileCopyUtils.copyToString(fr);
        kafkaTemplate.send(ORDER_KAFKA_TOPIC, message).get();
        Thread.sleep(3000);
        Mono<OrderModel> savedOrder = orderMongoRepository.findById(1);
        OrderModel order = savedOrder.block(Duration.ofSeconds(2L));
        assertTrue(Optional.ofNullable(order).isPresent());
        assertEquals(1, order.getClient().getId());

    }

    @Test
    public void testListenAndProcessMessageError() throws Exception {

        var file = new ClassPathResource("kafka/message_error.json").getFile();
        var fr = new FileReader(file);
        var message = FileCopyUtils.copyToString(fr);
        kafkaTemplate.send(ORDER_KAFKA_TOPIC, message).get();
        Thread.sleep(3000);
        Mono<OrderModel> savedOrder = orderMongoRepository.findById(1);
        OrderModel order = savedOrder.block(Duration.ofSeconds(2L));
        assertFalse(Optional.ofNullable(order).isPresent());

    }
}

