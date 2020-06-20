/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

/**
 * @author Yuriy Stul
 */
@ExtendWith(VertxExtension.class)
class KafkaVerticleTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaVerticleTest.class);

    @Mock
    KafkaConsumer<String, String> consumer;

    @Test
    @SuppressWarnings("unchecked")
    void dummyTest(Vertx vertx, VertxTestContext testContext) {
        consumer = mock(KafkaConsumer.class);
        var v = new KafkaVerticle(consumer);

        vertx.deployVerticle(v, dr -> testContext.completeNow());
    }

    @Test
    @SuppressWarnings("unchecked")
    void runTest(Vertx vertx, VertxTestContext testContext) {
        consumer = mock(KafkaConsumer.class);

        doAnswer(invocationOnMock -> {
            logger.debug("In invocation...");
            for (var arg : invocationOnMock.getArguments()) {
                logger.debug("{}", arg.getClass().getName());
            }
            logger.debug("method name: {}", invocationOnMock.getMethod().getName());
            Handler<KafkaConsumerRecord<String, String>> callback = invocationOnMock.getArgument(0);
            var record = new TestKafkaConsumerRecord<>("some key", "some value");
            callback.handle(record);
            return null;
        }).when(consumer).handler(any());

        var v = new KafkaVerticle(consumer);

        vertx.deployVerticle(v, dr -> vertx.setTimer(1000, l -> testContext.completeNow()));
    }
}