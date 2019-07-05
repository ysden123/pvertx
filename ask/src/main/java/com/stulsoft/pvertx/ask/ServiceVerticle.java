/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.ask;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ServiceVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVerticle.class);

    static final String EB_ADDRESS = "ServiceVerticle";


    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Starting ServiceVerticle ...");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    private void handler(Message<JsonObject> msg) {
        logger.info("ServiceVerticle: handling {}", msg.body().encodePrettily());

        vertx.setTimer(500, l -> {
            logger.info("ServiceVerticle: completed");
            msg.reply(new JsonObject().put("result", "Done 1"),
                    replyResult -> {
                        if (replyResult.succeeded())
                            logger.info("Succeeded to reply");
                        else
                            logger.error("Failed to reply {}", replyResult.cause().getMessage());
                    }
            );
        });
    }
}
