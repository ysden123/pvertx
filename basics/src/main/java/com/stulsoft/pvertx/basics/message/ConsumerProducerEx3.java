/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.message;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage the MessageConsumer and MessageProvider with flowable and response from message handler.
 *
 * @author Yuriy Stul
 */
public class ConsumerProducerEx3 {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerProducerEx3.class);
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
        logger.info("(1) Handling {}", msg.body());
        msg.reply("Handled " + msg.body());
    }

    private static void test1(final Vertx vertx) {
        logger.info("==>test1");
        vertx.eventBus()
                .<String>consumer(EB_CONSUMER)
                .toFlowable()
                .subscribe(ConsumerProducerEx3::consumerHandler);

        vertx.eventBus().<String>request(EB_CONSUMER, "msg1", r -> logger.info("(2) Result: {}", r.result().body()));
        vertx.eventBus().<String>request(EB_CONSUMER, "msg2", r -> logger.info("(2) Result: {}", r.result().body()));
        vertx.eventBus().<String>request(EB_CONSUMER, "msg3", r -> logger.info("(2) Result: {}", r.result().body()));
        vertx.eventBus().<String>request(EB_CONSUMER, "msg4", r -> logger.info("(2) Result: {}", r.result().body()));
        vertx.eventBus().<String>request(EB_CONSUMER, "msg5", r -> logger.info("(2) Result: {}", r.result().body()));

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
