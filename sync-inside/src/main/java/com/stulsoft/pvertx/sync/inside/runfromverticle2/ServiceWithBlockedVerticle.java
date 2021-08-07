/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.sync.inside.runfromverticle2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ServiceWithBlockedVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceWithBlockedVerticle.class);

    public static final String EB_ADDRESS = ServiceWithBlockedVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start(startPromise);

        BlockedService2 blockedService2 = new BlockedService2();
        blockedService2.run(vertx);
    }

    private void handler(Message<String> message) {
        logger.info("==>handler");
        logger.debug("Received message {}", message.body());
    }
}
