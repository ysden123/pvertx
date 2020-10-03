/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.msgqueue;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message producer and message consumer
 *
 * @author Yuriy Stul
 */
public class MsgConsumerProducerEx1 {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var msgQueueAddress = "msgQueueAddress";

        var vertx = Utils.createVertx();
        MessageProducer<String> msgProducer = vertx.eventBus().sender(msgQueueAddress);
        MessageConsumer<String> msgConsumer = vertx.eventBus().consumer(msgQueueAddress);

        msgConsumer.handler(handler -> logger.info("Received {}", handler.body()));

        for (int i = 1; i <= 5; ++i) {
            msgProducer.write("msg # " + i);
        }
    }
}
