/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.msgqueue;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.eventbus.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class MessageConsumerEx1 {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var address="messageQueue";

        var vertx = Utils.createVertx();

        MessageConsumer<String> messageConsumer = vertx.eventBus().consumer(address);
        messageConsumer.handler(handler -> {
            logger.info("Received {}", handler.body());
//            messageConsumer.pause();
        });

        for(int i = 1; i <= 5; ++i ) {
            vertx.eventBus().send(address, "test # " + i);
        }

        try{
            Thread.sleep(1000);
            vertx.close();
        }catch (Exception ignore){}
    }
}
