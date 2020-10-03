/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class VerticleForChain extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(VerticleForChain.class);

    public static final String EB_ADDRESS = VerticleForChain.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        logger.info("Starting");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Stopping");
    }

    private void handler(Message<String> msg) {
        logger.info("Handling [{}] message", msg.body());
        vertx.setTimer(500, l -> msg.reply("Done with " + msg.body()));
    }
}
