/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class Verticle4Multiple extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Verticle4Multiple.class);
    public static final String EB_ADDRESS = "Verticle4Multiple";
    private Random random = new Random();

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        logger.info("==>stop");
        stopPromise.complete();
    }

    private void handler(Message<String> msg) {
        try {
            logger.info("Handling {}", msg.body());
//            Thread.sleep(random.nextInt(600));
            Thread.sleep(500);
            msg.reply("Done");
        } catch (Exception ignore) {
        }
    }

}
