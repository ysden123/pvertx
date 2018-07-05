/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.deploy.options;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class V1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(V1.class);

    public static final String EB_ADDRESS = "v1";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.info("==>start with conf: {}", config());
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        logger.info("<==stop");
        super.stop();
    }

    private void handler(final Message<String> message) {
        logger.info("==>handler with message {}", message.body());
        var conf = config();
        logger.info("conf: {}", conf);
        logger.info("context.getInstanceCount() = {}",context.getInstanceCount());

        vertx.setTimer(300, l -> message.reply("Completed V1"));
    }
}
