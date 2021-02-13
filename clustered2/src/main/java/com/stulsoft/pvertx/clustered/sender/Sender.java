/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;

/**
 * Sender verticle
 *
 * @author Yuriy Stul
 */
public class Sender extends AbstractVerticle {

    public static final String EB_ADDRESS = "Sender";

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    private void handler(Message<String> msg) {
        final var eb = vertx.eventBus();
        eb.<String>request(
                Service.EB_ADDRESS,
                "some message ".concat(msg.body()),
                sendResult -> {
                    if (sendResult.succeeded())
                        msg.reply(sendResult.result().body());
                    else
                        msg.fail(1, sendResult.cause().getMessage());
                }
        );
    }
}
