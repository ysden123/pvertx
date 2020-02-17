package com.stulsoft.pvertx.unittest1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

/**
 * @author Yuriy Stul
 * @since 5/6/2018
 */
public class Service1 extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().consumer("service1", (message) -> message.reply("My reply"));
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        stopPromise.complete();
    }
}
