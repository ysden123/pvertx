/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.asynctest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Usage of blockinGet
 *
 * @author Yuriy Stul
 */
public class AsyncTestEx03 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTestEx03.class);
    private final Vertx vertx;
    public static String EB_ADDRESS = AsyncTestEx03.class.getName();
    private final Random random = new Random();
    private static final String ATTR_NUMBER = "number";
    private static final String ATTR_REPLY = "reply";

    public AsyncTestEx03(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }


    private void handler(Message<JsonObject> msg) {
        var delay = random.nextInt(1000) + 1;
        var number = msg.body().getInteger(ATTR_NUMBER);
        vertx.setTimer(delay, l -> {
            var reply = new JsonObject().put(ATTR_REPLY, "Replay for " + number);
            msg.reply(reply);
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var counter = new AtomicInteger(0);
        vertx.deployVerticle(new AsyncTestEx03(vertx), dr -> {
            try {
                final int maxCounter = 10;
                for (var i = 1; i <= maxCounter; ++i) {
                    var theI = i;
                    vertx.setTimer(500, l ->
                            vertx.executeBlocking(blockingCodeHandler -> {
                                JsonObject result = (JsonObject) vertx
                                        .eventBus()
                                        .rxRequest(EB_ADDRESS, new JsonObject().put(ATTR_NUMBER, theI))
                                        .blockingGet()
                                        .body();
                                var reply = result.getString(ATTR_REPLY);
                                logger.info("reply: {}", reply);
                                if (counter.incrementAndGet() >= maxCounter) {
                                    vertx.close();
                                    logger.info("<==main");
                                }
                            }, resultCodeHandler ->
                                    logger.debug("In resultCodeHandler"))
                    );
                }
            } catch (Exception ex) {
                logger.error("Failure 100: {}", ex.getMessage());
                vertx.close();
            }
        });
    }
}
