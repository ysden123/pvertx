/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.stulsoft.pvertx.kafka.Constants.EB_SERVICE_ADDRESS;

/**
 * Usual service
 *
 * @author Yuriy Stul
 */
public class ServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVerticle.class);

    private final Random random = new Random();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_SERVICE_ADDRESS, this::handler);
        logger.info("Started");
    }

    private void handler(Message<String> msg) {
        logger.info("Started handling {}", msg.body());
        vertx.setTimer(123 + random.nextInt(1000),
                l -> {
                    logger.info("Completed handling {}", msg.body());
                    msg.reply("Done");
                });
    }
}
