/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pqueue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class JobVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(JobVerticle.class);

    public static String EB_ADDRESS = "JobVerticle";

    private final Random random = new Random();

//    private int[] delays={500, 700, 800, 1000};
    private int[] delays={100, 200, 500, 700, 800, 1000};

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Starting JobVerticle ...");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        logger.info("Stopping JobVerticle ...");
        stopFuture.complete();
    }

    private void handler(Message<String> msg) {
        logger.info("Handling [{}] message", msg.body());
        vertx.setTimer(defineDelay(), l -> msg.reply("Done with " + msg.body()));
    }

    private long defineDelay() {
        return delays[random.nextInt(delays.length)];
    }
}
