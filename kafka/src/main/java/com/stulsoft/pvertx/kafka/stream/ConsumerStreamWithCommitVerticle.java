/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import com.stulsoft.pvertx.kafka.Config;
import com.stulsoft.pvertx.kafka.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import io.vertx.kafka.client.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.stulsoft.pvertx.kafka.Constants.EB_SERVICE_ADDRESS;
import static com.stulsoft.pvertx.kafka.Constants.TOPIC_NAME;

/**
 * Vertx Kafka consumer with pause, resume, and commit. Guarantees sequential processing.
 *
 * @author Yuriy Stul
 */
public class ConsumerStreamWithCommitVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerStreamWithCommitVerticle.class);

    private KafkaReadStream<String, String> streamReader;
    private KafkaConsumer<String, String> consumer;

    @Override
    public void start() throws Exception {
        super.start();

        var config = new HashMap<String, String>();
        config.put("bootstrap.servers", Config.kafkaUrl());
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "stream_test_group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        consumer = KafkaConsumer.create(vertx, config);

        streamReader = consumer.asStream();
        streamReader.handler(this::handler);

        consumer.subscribe(Constants.TOPIC_NAME);
        logger.info("Started");
    }

    private void handler(ConsumerRecord<String, String> record) {
        logger.info("Received {}", record.value());
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
                    commit(record);
                });
    }

    private void commit(ConsumerRecord<String, String> record) {
        var offsets = new HashMap<TopicPartition, OffsetAndMetadata>();
        var partition = record.partition();
        var offset = record.offset() + 1;
        var topicPartition = new TopicPartition(TOPIC_NAME, partition);
        var offsetAndMetadata = new OffsetAndMetadata(offset, null);
        offsets.put(topicPartition, offsetAndMetadata);
        logger.info("Committing {} with partition = {}, offset = {}",
                record.value(), partition, offset);
        consumer.commit(offsets,
                ar -> {
                    if (ar.succeeded()) {
                        logger.info("Committed {} with partition = {}, offset = {}",
                                record.value(), partition, offset);
                    } else {
                        logger.error("Failed committing {} with partition = {}, offset = {}. Error: {}",
                                record.value(), partition, offset, ar.cause().getMessage());
                    }
                });
    }
}
