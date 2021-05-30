/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.vertx4.future;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Service2Verticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Service2Verticle.class);

    public static final String EB_ADDRESS = Service2Verticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start(startPromise);
    }

    private void handler(Message<String> message) {
        logger.info("==>handler");
        vertx.setTimer(1000, __ -> {
            if (message.body().contains("err2"))
                message.fail(1, "Error 2");
            else
                message.reply("Done for " + message.body());
        });

    }
}
