/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.deployment.start;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Verticle2 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Verticle2.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.debug("Starting {}", this.getClass().getSimpleName());
        vertx.eventBus().consumer("START", this::processStart);
        startPromise.complete();
    }

    private void processStart(Message<?> message) {
        logger.info("==>processStart");
        vertx.setPeriodic(500, __ -> logger.info("Doing a work"));
    }
}
