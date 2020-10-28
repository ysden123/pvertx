/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.delivery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

/**
 * @author Yuriy Stul
 */
public class ServiceForDelivery extends AbstractVerticle {
    public static final String EB_ADDRESS = ServiceForDelivery.class.getName();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<String> message) {
        vertx.setTimer(5_000L, l -> {
            System.out.println("In handler. Done for " + message.body());
            message.reply("Done for " + message.body());
        });
    }
}
