/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiinst;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ServiceForMultiInst extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceForMultiInst.class);
    public static final String EB_ADDRESS = ServiceForMultiInst.class.getName();

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("==>start with conf: {}", config());
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    @Override
    public void stop() throws Exception {
        logger.info("<==stop");
        super.stop();
    }

    private void handler(final Message<String> message) {
        logger.info("==>handler with message {}", message.body());
        message.reply("Completed");
    }
}
