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
public class EBServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(EBServiceVerticle.class);

    public static String EB_ADDRESS = EBServiceVerticle.class.getName();

    private Random random = new Random();

    @Override
    public void start() throws Exception {
        super.start();

        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void handler(Message<String> msg) {
        vertx.executeBlocking(future -> {
                    vertx.setTimer(123 + random.nextInt(1000), l -> future.complete("Done for " + msg.body()));
                },
                result -> {
                    if (result.succeeded()) {
                        msg.reply(result.result());
                    }
                });
    }
}
