/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.header;

import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates usage the message header
 *
 * @author Yuriy Stul
 */
public class VerticleUseHeader extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(VerticleUseHeader.class);

    private static final String EB_ADDRESS = "VerticleUseHeader";
    private static final String HEADER_ACTION = "action";
    private static final String ACTION_TEST1 = "test1";
    private static final String ACTION_TEST2 = "test2";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        logger.info("==>stop");
        stopFuture.complete();
    }

    private void handler(Message<JsonObject> message) {
        switch (message.headers().get(HEADER_ACTION)) {
            case ACTION_TEST1:
                handleTest1(message);
                break;
            case ACTION_TEST2:
                handleTest2(message);
                break;
            default:
                message.fail(404, "Unsupported action " + message.headers().get(HEADER_ACTION));
        }
    }

    private void handleTest1(Message<JsonObject> message) {
        message.reply(new JsonObject()
                .put("action_name", ACTION_TEST1)
                .put("result", "The result for " + ACTION_TEST1)
        );
    }

    private void handleTest2(Message<JsonObject> message) {
        message.reply(new JsonObject()
                .put("action_name", ACTION_TEST2)
                .put("result", "The result for " + ACTION_TEST2)
        );
    }

    public static void main(String[] args) throws Exception {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        vertx.rxDeployVerticle(VerticleUseHeader.class.getName())
                .doOnSuccess(s -> {
                    var message = new JsonObject();
                    vertx.eventBus().rxSend(EB_ADDRESS, message, new DeliveryOptions()
                            .addHeader(HEADER_ACTION, ACTION_TEST1))
                            .subscribe(
                                    result -> logger.info(result.body().toString()),
                                    error -> logger.error(error.getCause().getMessage()));
                })
                .doOnSuccess(s -> {
                    var message = new JsonObject();
                    vertx.eventBus().rxSend(EB_ADDRESS, message, new DeliveryOptions()
                            .addHeader(HEADER_ACTION, ACTION_TEST2))
                            .subscribe(
                                    result -> logger.info(result.body().toString()),
                                    error -> logger.error(error.getCause().getMessage()));
                })
                .doOnError(error -> logger.error(error.getCause().getMessage()))
                .subscribe();
        Thread.sleep(1000);
        vertx.close();
        logger.info("<==main");
    }
}
