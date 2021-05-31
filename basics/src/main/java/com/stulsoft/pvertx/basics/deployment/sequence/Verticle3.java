/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.deployment.sequence;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Verticle3 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Verticle3.class);

    @Override
    public void start() throws Exception {
        logger.debug("Starting {}", this.getClass().getSimpleName());
        vertx.setTimer(1000, __ -> logger.debug("Start is complete"));
    }
}
