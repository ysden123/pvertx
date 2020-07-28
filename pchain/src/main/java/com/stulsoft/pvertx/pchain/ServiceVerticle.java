/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pchain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class ServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVerticle.class);

    public static String EB_ADDRESS = ServiceVerticle.class.getName();

    private final Random random = new Random();

    @Override
    public void start() throws Exception {
        logger.info("Starting...");

        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping...");
        super.stop();
    }

    private void handler(Message<String> msg) {
        logger.info("Handling {}", msg.body());
        final long start = System.currentTimeMillis();
        vertx.setTimer(500 + random.nextInt(1000), l -> {
            logger.info("Handled {} in {} ms", msg.body(), System.currentTimeMillis() - start);
            msg.reply("Handled " + msg.body());
        });
    }
}
