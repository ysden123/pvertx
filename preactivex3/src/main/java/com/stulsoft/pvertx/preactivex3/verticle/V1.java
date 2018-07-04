/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex3.verticle;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class V1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(V1.class);

    public static final String EB_ADDRESS = "v1_verticle";

    @Override
    public void start() throws Exception {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start();
    }

    private void handler(final Message<String> message) {
        message.reply("v1");
    }
}
