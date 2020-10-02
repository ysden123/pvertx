/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.stulsoft.pvertx.kafka.Constants.EB_SERVICE_ADDRESS;

/**
 * Service with exception in main thread
 *
 * @author Yuriy Stul
 */
public class ServiceWithException2Verticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceWithException2Verticle.class);

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_SERVICE_ADDRESS, this::handler);
        logger.info("Started");
    }

    private void handler(Message<String> msg) {
        logger.info("Started handling {}", msg.body());
        if ("test # 3".equals(msg.body())) {
            logger.error("Test error");
            throw new RuntimeException("Test exception");
        } else {
            logger.info("Completed handling {}", msg.body());
            msg.reply("Done");
        }
    }
}
