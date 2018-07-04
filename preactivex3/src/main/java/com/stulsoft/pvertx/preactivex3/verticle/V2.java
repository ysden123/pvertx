/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex3.verticle;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.reactivex.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class V2 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(V2.class);

    public static final String EB_ADDRESS = "v2_verticle";

    @Override
    public void init(Vertx vertx, Context context) {
        logger.info("==>init");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.init(vertx, context);
    }

    private void handler(final io.vertx.core.eventbus.Message<String> message) {
        message.reply("v2");
    }
}
