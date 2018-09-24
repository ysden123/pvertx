/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.message;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage the MessageConsumer and MessageProvider with flowable.
 *
 * @author Yuriy Stul
 */
public class ConsumerProducerEx2 {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerProducerEx2.class);
    private static final String EB_CONSUMER = "consumer";

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        test1(vertx);
        test2(vertx);

        vertx.setTimer(1000, l -> vertx.close());
        logger.info("<==main");
    }

    private static void consumerHandler(Message<String> msg) {
        logger.info("Handling {}", msg.body());
    }

    private static void test1(final Vertx vertx) {
        logger.info("==>test1");
        vertx.eventBus()
                .<String>consumer(EB_CONSUMER)
                .toFlowable()
                .subscribe(ConsumerProducerEx2::consumerHandler);

        vertx.eventBus().send(EB_CONSUMER, "msg1");
        vertx.eventBus().send(EB_CONSUMER, "msg2");
        vertx.eventBus().send(EB_CONSUMER, "msg3");
        vertx.eventBus().send(EB_CONSUMER, "msg4");
        vertx.eventBus().send(EB_CONSUMER, "msg5");

        logger.info("<==test1");
    }

    private static void test2(final Vertx vertx) {
        logger.info("==>test2");
        var publisher = vertx.eventBus().<String>publisher(EB_CONSUMER);
        publisher.write("published msg1");
        publisher.write("published msg2");
        publisher.write("published msg3");
        publisher.write("published msg4");
        logger.info("<==test2");
    }
}
