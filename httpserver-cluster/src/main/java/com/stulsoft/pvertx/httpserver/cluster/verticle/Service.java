/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.httpserver.cluster.verticle;

import io.vertx.core.Promise;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class Service extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    private static Random random = new Random(System.currentTimeMillis());  // Bad code! Verticle should be stateless!

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        logger.info("Starting Service...");

        vertx.eventBus().consumer("serviceAddress", this::handleMessage);

        super.start(startFuture);
    }

    private void handleMessage(Message<String> message) {
        logger.info("Service is running...");
        vertx.setTimer(500, l -> {
            if (random.nextBoolean())
                message.reply("My reply " + random.nextInt());
            else
                message.fail(1, "Something went wrong");
        });

    }
}
