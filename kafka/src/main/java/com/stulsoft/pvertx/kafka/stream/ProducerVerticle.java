/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import com.stulsoft.pvertx.kafka.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.stulsoft.pvertx.kafka.Constants.TOPIC_NAME;

/**
 * Sends messages to Kafka
 *
 * @author Yuriy Stul
 */
public class ProducerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ProducerVerticle.class);

    @Override
    public void start() throws Exception {
        super.start();
        var config = new HashMap<String, String>();
        config.put("bootstrap.servers", Config.kafkaUrl());
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");
        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config);

        for (int i = 1; i <= 20; ++i) {
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create(TOPIC_NAME, "test # " + i);
            producer.write(record);
        }
        logger.info("Started");
    }
}
