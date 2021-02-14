/*
 * Copyright (c) 2021. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.scope;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ScopeTestVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ScopeTestVerticle.class);

    public static final String EB_ADDRESS_1 = ScopeTestVerticle.class.getName().concat("_1");
    public static final String EB_ADDRESS_2 = ScopeTestVerticle.class.getName().concat("_2");

    @Override
    public void start() throws Exception {
        super.start();
        logger.info("Started {}", this.getClass().getSimpleName());
        vertx.eventBus().consumer(EB_ADDRESS_1, this::handle);
        JsonObject someObject = new JsonObject().put("a", 123);
        int i = 123;

        vertx.eventBus().<String>consumer(EB_ADDRESS_2, handler -> {
            handler.reply("Out of scope handling for ".concat(handler.body()) + " " + someObject.encode() + ", " + i);
        });


//        someObject.remove("a");
    }

    private void handle(Message<String> message) {
        logger.info("Received request: {}", message.body());

        message.reply("Finished handling ".concat(message.body()));
    }

    public static void main(String[] args) {
        var vertx = Utils.createVertx();
        vertx.deployVerticle(ScopeTestVerticle.class.getName());

        vertx.setTimer(1000, l->{
            vertx.eventBus().request(EB_ADDRESS_1, "test 1", result -> {
                logger.info("Response: {}", result.result().body());
            });
        });

        vertx.setTimer(2000, l -> {
            logger.info("Running GC");
            Runtime.getRuntime().gc();
            logger.info("GC finished");
        });

        vertx.setTimer(4000, l -> {
            vertx.eventBus().request(EB_ADDRESS_2, "test 2", result -> {
                logger.info("Response: {}", result.result().body());
            });
        });
    }
}
