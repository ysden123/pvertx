/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pchain;

import io.reactivex.Single;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ChainMngr3Verticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ChainMngr3Verticle.class);

    public static final String EB_ADDRESS = ChainMngr3Verticle.class.getName();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
        logger.info("Starting ...");

        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
        logger.info("Stopping ...");
    }

    private void handler(Message<String> message) {
        var messages = Utils.generateMsg(10);

        @SuppressWarnings("unchecked")
        Single<String>[] works = new Single[messages.length];
        for (int i = 0; i < messages.length; ++i)
            works[i] = makeWork(messages[i]);

        Single.concatArray(works)
                .last("")
                .subscribe(
                        message::reply,
                        err -> message.fail(123, err.getMessage())
                );

    }

    private Single<String> makeWork(String message) {
        return Single.create(source -> vertx.eventBus().request(
                ServiceVerticleWithError.EB_ADDRESS,
                message,
                ar -> {
                    if (ar.succeeded())
                        source.onSuccess(ar.result().body().toString());
                    else
                        source.onError(ar.cause());
                }));
    }
}
