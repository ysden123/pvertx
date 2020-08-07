/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.exception;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unhandled exception
 *
 * @author Yuriy Stul
 */
public class VerticleWithException extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(VerticleWithException.class);

    public static final String EB_ADDRESS = VerticleWithException.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        logger.info("Starting...");
        vertx.eventBus().consumer(EB_ADDRESS, this::handlerWithException);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Stopping...");
    }

    private void handlerWithException(Message<String> message) {
        throw new RuntimeException("Test exception for " + message.body());
    }
}
