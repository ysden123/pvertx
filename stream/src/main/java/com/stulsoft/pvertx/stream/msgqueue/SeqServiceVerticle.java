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
 * Sequential service: guarantees sequential processing of input messages
 *
 * @author Yuriy Stul
 */
public class SeqServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SeqServiceVerticle.class);
    public static final String EB_ADDRESS = SeqServiceVerticle.class.getName();
    private final Random random = new Random();
    private MessageConsumer<String> messageConsumer;

    @Override
    public void start() throws Exception {
        super.start();
        messageConsumer = vertx.eventBus().<String>consumer(EB_ADDRESS);
        messageConsumer.handler(this::handler);
        logger.info("Started");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Stopped");
    }

    private void handler(Message<String> message) {
        messageConsumer.pause();
        logger.info("Handling {}", message.body());
        vertx.setTimer(123 + random.nextInt(1000),
                l -> {
                    logger.info("Completed handling for {}", message.body());
                    message.reply("Done for " + message.body());
                    messageConsumer.resume();
                });
    }
}
