/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.asynctest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yuriy Stul
 */
public class AsyncTestEx01 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTestEx01.class);
    private final Vertx vertx;
    public static String EB_ADDRESS = "AsyncTestEx01";
    private final Random random = new Random();
    private static final String ATTR_NUMBER = "number";
    private static final String ATTR_REPLY = "number";

    public AsyncTestEx01(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void start(Promise<Void> startPromise){
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }


    private void handler(Message<JsonObject> msg) {
        var delay = random.nextInt(1000);
        var number = msg.body().getInteger(ATTR_NUMBER);
//        logger.info("(1) number = {}", number);
        vertx.setTimer(delay, l -> {
//            logger.info("(2) number = {}", number);
            var reply = new JsonObject().put(ATTR_REPLY, "Replay for " + number);
            msg.reply(reply);
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var counter = new AtomicInteger(0);
        vertx.deployVerticle(new AsyncTestEx01(vertx), dr -> {
            final int maxCounter = 10;
            for (var i = 1; i <= maxCounter; ++i) {
                vertx.eventBus().request(EB_ADDRESS, new JsonObject().put(ATTR_NUMBER, i), r -> {
                    var reply = ((JsonObject) r.result().body()).getString(ATTR_REPLY);
                    logger.info("reply: {}", reply);
                    if (counter.incrementAndGet() >= maxCounter){
                        vertx.close();
                        logger.info("<==main");
                    }
                });
            }
        });
    }
}
