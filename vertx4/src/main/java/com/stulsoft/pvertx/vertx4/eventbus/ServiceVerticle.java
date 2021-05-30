/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.vertx4.eventbus;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVerticle.class);

    public static final String EB_ADDRESS = ServiceVerticle.class.getName();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start();
    }

    private void handler(Message<String> message) {
        message.replyAndRequest("Done for " + message.body(),
                ar -> {
                    if (ar.succeeded()) {
                        logger.info("Succeeded reply: {}", ar.result().body().toString());
                    } else {
                        logger.error("Failed reply: {}", ar.cause().getMessage());
                    }
                });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.deployVerticle(new ServiceVerticle(), dr -> {
/*
            vertx.eventBus().request(EB_ADDRESS, "Test 1", ar -> {
                if (ar.succeeded())
                    logger.info("Response: {}", ar.result().body().toString());
                else
                    logger.error("Failed response: {}", ar.cause().getMessage());
                ar.result().reply("Received");
                vertx.close();
            });
*/

            vertx.eventBus().request(EB_ADDRESS, "Test 2")
                    .onComplete(ar ->{
                        if (ar.succeeded())
                            logger.info("Response: {}", ar.result().body().toString());
                        else
                            logger.error("Failed response: {}", ar.cause().getMessage());
                        ar.result().reply("Received");
                        vertx.close();
                    });
        });
    }
}
