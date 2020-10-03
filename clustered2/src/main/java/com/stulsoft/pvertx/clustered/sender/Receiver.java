/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receives a messages
 *
 * @author Yuriy Stul
 */
public class Receiver extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    public static final String EB_ADDRESS = "Receiver";

    @Override
    public void start(Promise<Void> startPromise){
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    private void handler(Message<String> msg) {
        logger.info("Received: {}", msg.body());
    }
}
