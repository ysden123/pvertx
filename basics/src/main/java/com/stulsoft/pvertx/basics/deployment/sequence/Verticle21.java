/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.deployment.sequence;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Verticle21 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Verticle21.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.debug("Starting {}", this.getClass().getSimpleName());
        vertx.setTimer(3000,
                __ -> {
                    logger.debug("Start is complete");
                    startPromise.complete();
                });
    }
}
