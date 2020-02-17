/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiple;

import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates handling of many messages by one instqnce of verticle
 *
 * @author Yuriy Stul
 */
public class MultipleRunner {
    private static final Logger logger = LoggerFactory.getLogger(MultipleRunner.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.basics.multiple.Verticle4Multiple", deployResult -> {
            if (deployResult.succeeded()) {
                logger.info("Deployed");

                for (int i = 1; i <= 5; ++i) {
                    var ii = i;
                    vertx.eventBus().request(Verticle4Multiple.EB_ADDRESS, "msg # " + i,
                            result -> logger.info("result[{}]: {}", ii, result.result().body().toString()));
                }
            } else {
                logger.error("Failed deployment. {}", deployResult.cause().getMessage());
            }
        });

        try {
            Thread.sleep(3000);
            vertx.close();
        } catch (Exception ignore) {

        }

        logger.info("<==main");
    }
}
