/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pqueue;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
*/

/**
 * @author Yuriy Stul
 */
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        var vertx = Vertx.vertx();

        vertx.deployVerticle(ReceiveVerticle.class.getName());

        vertx.deployVerticle(JobVerticle.class.getName(), dr -> {
            for (var i = 1; i <= 5; ++i) {
                var theI = i;

                vertx.<String>executeBlocking(bh -> {
                            var msgText = "Message " + theI;
                            vertx.eventBus().send(JobVerticle.EB_ADDRESS, msgText, ar -> {
                                if (ar.succeeded()) {
                                    logger.info("Handled {} : {}", msgText, ar.result().body().toString());
                                } else {
                                    logger.error("Failed to handle {} : {}", msgText, ar.cause().getMessage());
                                }
                            });
                            bh.complete();
                        },
                        rest -> {

                        });

/*
                vertx.<String>executeBlocking(bh -> {
                            var msgText = "Message " + theI;
                            vertx.eventBus().send(JobVerticle.EB_ADDRESS, msgText, ar -> {
                                if (ar.succeeded()) {
                                    logger.info("Handled {} : {}", msgText, ar.result().body().toString());
                                } else {
                                    logger.error("Failed to handle {} : {}", msgText, ar.cause().getMessage());
                                }
                            });
                            bh.complete();
                        },
                        true,
                        rest -> {

                        });
*/
            }

        });


        vertx.setTimer(10000, l -> vertx.close());
    }
}
