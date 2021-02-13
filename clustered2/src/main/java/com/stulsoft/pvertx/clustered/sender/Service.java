/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;

/**
 * Receives a messages
 *
 * @author Yuriy Stul
 */
public class Service extends AbstractVerticle {
    public static final String EB_ADDRESS = "Service";
    public final String serviceInstanceName;

    public Service(final String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    private void handler(Message<String> msg) {
        var response = String.format("[%s] has been handled by %s", msg.body(), serviceInstanceName);
        msg.reply(response);
    }
}
