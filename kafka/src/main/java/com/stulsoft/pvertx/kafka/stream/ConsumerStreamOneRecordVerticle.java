/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import com.stulsoft.pvertx.kafka.Config;
import com.stulsoft.pvertx.kafka.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.stulsoft.pvertx.kafka.Constants.EB_SERVICE_ADDRESS;

/**
 * Vertx Kafka consumer with pause and resume. Guarantees sequential processing.
 * max.poll.records = 1
 * @author Yuriy Stul
 */
public class ConsumerStreamOneRecordVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerStreamOneRecordVerticle.class);

    private KafkaReadStream<String, String> streamReader;

    @Override
    public void start() throws Exception {
        super.start();

        var config = new HashMap<String, String>();
        config.put("bootstrap.servers", Config.kafkaUrl());
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "stream_test_group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "true");
        config.put("max.poll.records", "1");

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

        streamReader = consumer.asStream();
        streamReader.handler(this::handler);

        consumer.subscribe(Constants.TOPIC_NAME);
        logger.info("Started");
    }

    private void handler(ConsumerRecord<String, String> record) {
        logger.info("Received {}, partition={}, offset={}",
                record.value(), record.partition(), record.offset());
        streamReader.pause();

        vertx.eventBus().<String>request(EB_SERVICE_ADDRESS,
                record.value(),
                result -> {
                    if (result.succeeded()) {
                        logger.info("Response for {} is {}", record.value(), result.result().body());
                    } else {
                        logger.error("Failed processing {} message. {}",
                                record.value(), result.cause().getMessage());
                    }
                    streamReader.resume();
                });
    }
}
