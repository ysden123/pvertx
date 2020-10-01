/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.stulsoft.pvertx.kafka.Constants.BROCKER_URL;

/**
 * Vertx Kafka consumer with pause and resume. Guarantees sequential processing.
 *
 * @author Yuriy Stul
 */
public class ConsumerStreamVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerStreamVerticle.class);

    private KafkaConsumer<String, String> consumer;
    private KafkaReadStream<String, String> streamReader;

    @Override
    public void start() throws Exception {
        super.start();

        var config = new HashMap<String, String>();
        config.put("bootstrap.servers", BROCKER_URL);
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "stream_test_group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "true");
        consumer = KafkaConsumer.create(vertx, config);

        streamReader = consumer.asStream();
        consumer.asStream().handler(this::handler);

        consumer.subscribe(Constants.TOPIC_NAME);
        logger.info("Started");
    }

    private void handler(ConsumerRecord<String, String> record) {
        logger.info("Received {}", record.value());
        streamReader.pause();

        vertx.eventBus().<String>request(ServiceVerticle.EB_ADDRESS,
                record.value(),
                result -> {
                    logger.info("Response for {} is {}", record.value(), result.result().body());
                    streamReader.resume();
                });
    }
}
