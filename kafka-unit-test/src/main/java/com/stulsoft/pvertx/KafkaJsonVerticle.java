/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuriy Stul
 */
public class KafkaJsonVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(KafkaJsonVerticle.class);
    public static final String TOPIC = KafkaJsonVerticle.class.getSimpleName();

    private KafkaConsumer<String, JsonObject> consumer;

    public KafkaJsonVerticle() {
    }

    public KafkaJsonVerticle(final KafkaConsumer<String, JsonObject> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void start(Promise<Void> startPromise){
        logger.info("Starting {}", this.getClass().getSimpleName());

        if (consumer == null) {
            logger.debug("Creating Kafka consumer...");
            Map<String, String> config = new HashMap<>();
            config.put("bootstrap.servers", "localhost:9092");
            config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            config.put("group.id", "my_group");
            config.put("auto.offset.reset", "earliest");
            config.put("enable.auto.commit", "false");
            consumer = KafkaConsumer.create(vertx, config);
        }

        consumer.handler(this::handler);
        consumer.subscribe(TOPIC);
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise){
        logger.info("Stopping {}", this.getClass().getSimpleName());
        consumer.unsubscribe();
        consumer.close();
        stopPromise.complete();
    }

    private void handler(KafkaConsumerRecord<String, JsonObject> record) {
        logger.debug("Received {} -> {}", record.key(), record.value().encode());
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        vertx.deployVerticle(KafkaJsonVerticle.class.getName(), ar -> {
            if (ar.succeeded()) {
                vertx.setTimer(1000,
                        l -> {
                            vertx.close();
                            logger.info("<==main");
                        });
            } else {
                logger.error("Deployment failed: " + ar.cause().getMessage(), ar.cause());
                vertx.close();
                logger.info("<==main");
            }
        });
    }
}
