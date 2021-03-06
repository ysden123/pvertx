/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.msgqueue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Not sequential service: doesn't guarantee sequential processing of input messages
 *
 * @author Yuriy Stul
 */
public class NotSeqServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(NotSeqServiceVerticle.class);
    public static final String EB_ADDRESS = NotSeqServiceVerticle.class.getName();
    private final Random random = new Random();

    @Override
    public void start() throws Exception {
        super.start();
        MessageConsumer<String> messageConsumer = vertx.eventBus().consumer(EB_ADDRESS);
        messageConsumer.handler(this::handler);
        logger.info("Started");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Stopped");
    }

    private void handler(Message<String> message) {
        logger.info("Handling {}", message.body());
        vertx.setTimer(123 + random.nextInt(1000),
                l -> {
                    logger.info("Completed handling for {}", message.body());
                    message.reply("Done for " + message.body());
                });
    }
}
