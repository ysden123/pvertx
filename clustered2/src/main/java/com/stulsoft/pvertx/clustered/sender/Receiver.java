/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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

    /**
     * Start the verticle.<p>
     * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.<p>
     * If your verticle does things in its startup which take some time then you can override this method
     * and call the startFuture some time later when start up is complete.
     *
     * @param startFuture a future which should be called when verticle start-up is complete.
     */
    @Override
    public void start(Future<Void> startFuture) {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    private void handler(Message<String> msg) {
        logger.info("Received: {}", msg.body());
    }
}
