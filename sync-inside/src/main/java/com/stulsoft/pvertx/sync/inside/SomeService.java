/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.sync.inside;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SomeService extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SomeService.class);

    public static final String EB_ADDRESS = SomeService.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start(startPromise);
    }

    private void handler(Message<String> message){
        logger.info("==>handler");
        logger.debug("Received message {}", message.body());
    }
}
