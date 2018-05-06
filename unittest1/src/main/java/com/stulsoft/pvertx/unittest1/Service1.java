package com.stulsoft.pvertx.unittest1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/6/2018
 */
public class Service1 extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(Service1.class);

    @Override
    public void start(Future<Void> startFuture) {
        vertx.eventBus().consumer("service1", (message) -> {
            message.reply("My reply");
        });
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
