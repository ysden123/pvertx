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
 * Message producer and message consumer with pause and resume
 *
 * @author Yuriy Stul
 */
public class MsgConsumerProducerEx2 {
    private static final Logger logger = LoggerFactory.getLogger(MsgConsumerProducerEx2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var msgQueueAddress = "msgQueueAddress";

        var vertx = Utils.createVertx();
        MessageProducer<String> msgProducer = vertx.eventBus().sender(msgQueueAddress);
        msgProducer.setWriteQueueMaxSize(3);
        MessageConsumer<String> msgConsumer = vertx.eventBus().consumer(msgQueueAddress);

        msgConsumer.handler(handler -> {
            logger.info("Received {}", handler.body());
            msgConsumer.pause();
            vertx.setTimer(1000, l -> msgConsumer.resume());
        });

        for (int i = 1; i <= 5; ++i) {
            var msg = "msg # " + i;
            msgProducer.write("msg # " + i);
            logger.info("wrote {}", msg);
        }
    }
}
