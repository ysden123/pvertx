/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.shared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVerticle.class);

    public static final String EB_ADDRESS = ServiceVerticle.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        logger.info("{} has been started", this.getClass().getSimpleName());
    }

    private void handler(Message<String> message) {
        logger.info("Handling {}", message.body());
        message.reply("Handled " + message.body());
    }
}
