/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * @author Yuriy Stul
 */
@ExtendWith(VertxExtension.class)
public class KafkaJsonVerticleTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaJsonVerticleTest.class);

    @Mock
    KafkaConsumer<String, JsonObject> consumer;

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
            Handler<KafkaConsumerRecord<String, JsonObject>> callback = invocationOnMock.getArgument(0);
            var msg = Utils.jsonFromResource("msg1.json");
            var record = new TestKafkaConsumerRecord<String, JsonObject>("test key 1", msg);
            callback.handle(record);
            return null;
        }).when(consumer).handler(any());

        var v = new KafkaJsonVerticle(consumer);
        vertx.deployVerticle(v, dr -> vertx.setTimer(1000, l -> testContext.completeNow()));
    }

}
