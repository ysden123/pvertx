/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sender verticle
 *
 * @author Yuriy Stul
 */
public class Sender extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    public static final String EB_ADDRESS = "Sender";

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    private void handler(Message<String> msg) {
        final var eb = vertx.eventBus();
        logger.info("Received {}", msg.body());
        eb.publish(Receiver.EB_ADDRESS, "some message " + msg.body());
    }
}
