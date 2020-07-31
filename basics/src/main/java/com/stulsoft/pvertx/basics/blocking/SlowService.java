/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class SlowService extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SlowService.class);

    private final Random random = new Random();

    private final int n = 100000000;

    public static String EB_ADDRESS_BLOCKING = SlowService.class.getName() + "_blocking";
    public static String EB_ADDRESS_NO_BLOCKING = SlowService.class.getName() + "_noblocking";

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(EB_ADDRESS_BLOCKING, this::blockingHandler);
        vertx.eventBus().consumer(EB_ADDRESS_NO_BLOCKING, this::noBlockingHandler);
        super.start();
    }

    // Usage of vertx.executeBlocking warning messages from Vertx about blocked thread
    private void noBlockingHandler(Message<String> msg) {
        vertx.executeBlocking(
                future -> {
                    for (int i = 0; i < n; ++i) {
                        var ignore = Math.sin(random.nextDouble()) * Math.cos(random.nextDouble());
                    }
                    future.complete("Completed " + msg.body());
                },
                result -> {
                    logger.info("Completed handling {}", msg.body());
                    msg.reply(result.result());
                });
    }

    // Throws warning messages from Vertx about blocked thread
    private void blockingHandler(Message<String> msg) {
        for (int i = 0; i < n; ++i) {
            var ignore = Math.sin(random.nextDouble()) * Math.cos(random.nextDouble());
        }
        logger.info("Completed handling {}", msg.body());
        msg.reply("Completed " + msg.body());
    }
}
