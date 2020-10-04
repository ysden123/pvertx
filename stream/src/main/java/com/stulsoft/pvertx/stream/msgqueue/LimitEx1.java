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
 * Playing with limitations on queue size.
 *
 * @author Yuriy Stul
 */
public class LimitEx1 {
    private static final Logger logger = LoggerFactory.getLogger(LimitEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var msgQueueAddress = "msgQueueAddress.LimitEx1";

        var vertx = Utils.createVertx();
        MessageProducer<String> msgProducer = vertx.eventBus().sender(msgQueueAddress);
        MessageConsumer<String> msgConsumer = vertx.eventBus().consumer(msgQueueAddress);
        msgConsumer.setMaxBufferedMessages(3);
        msgProducer.setWriteQueueMaxSize(2);

        msgConsumer.handler(handler -> {
            logger.info("Received {}", handler.body());
            msgConsumer.pause();

            vertx.setTimer(500, l -> msgConsumer.resume());
        });


        for (int i = 1; i <= 10; ++i) {
            msgProducer.write("msg # " + i);
            logger.info("msgProducer.writeQueueFull() = {}", msgProducer.writeQueueFull());
        }

    }
}
