/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.generators;

import io.reactivex.Observable;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class IntGenVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(IntGenVerticle.class);

    private static final String EB_ADDRESS = "IntGenVerticle";
    private static final String COMMAND_COUNTER = "counter";
    private static final String COMMAND_GENERATE = "generate";
    private static final String RESULT = "result";

    @Override
    public void start(Future<Void> startFuture) {
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
        logger.info("<==start");
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        logger.info("==>stop");
        stopFuture.complete();
    }

    private void handler(final Message<String> message) {
        logger.info("==>handler {}", message.body());
        try {
            switch (message.body()) {
                case COMMAND_COUNTER:
                    logger.debug("Case {}", COMMAND_COUNTER);
//                message.rxReply(1);
                    message.reply(1);
                    break;
                case COMMAND_GENERATE:
                    logger.debug("Case {}", COMMAND_GENERATE);
                    vertx.eventBus().send(EB_ADDRESS, RESULT);
                    vertx.eventBus().send(EB_ADDRESS, RESULT);
                    vertx.eventBus().send(EB_ADDRESS, RESULT);
/*
                    message.reply(1);
                    message.reply(2);
                    message.reply(3);
*/
                    break;
                case RESULT:
                    logger.info("result = {}", message.body());
                    break;
                default:
                    logger.error("Undefined command {}", message.body());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    static void testWithoutChain() {
        logger.info("==>testWithoutChain");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(IntGenVerticle.class.getName(),
                deployResult -> {
                    logger.info("Deployed!");
                    vertx
                            .eventBus()
                            .send(EB_ADDRESS, COMMAND_COUNTER, response -> {
                                logger.info("Received counter {}", response.result().body());
                            });
                }
        );

        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }

        vertx.close();

        logger.info("<==testWithoutChain");
    }

    static void testWithChain() {
        logger.info("==>testWithChain");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(IntGenVerticle.class.getName(),
                deployResult -> {
                    logger.info("Deployed!");
                    vertx
                            .eventBus()
                            .rxSend(EB_ADDRESS, COMMAND_COUNTER)
                            .flatMap(counterObject -> {
                                logger.info("Counter = {}", counterObject.body());
//                                return vertx.eventBus().rxSend(EB_ADDRESS, COMMAND_GENERATE).toObservable();
                                return vertx.eventBus().rxSend(EB_ADDRESS, COMMAND_GENERATE);
                            })
                            .toObservable()
                            .subscribe(rr -> logger.info("rr {}", rr.body()));
/*
                            .subscribe(
                                    itemObject -> logger.info("item = {}", itemObject.body()),
                                    error -> logger.error("{}", error.getMessage())
                            );
*/
                });

        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }

        vertx.close();

        logger.info("<==testWithChain");
    }

    public static void main(String[] args) {
        logger.info("==>main");
//        testWithoutChain();
        testWithChain();
        logger.info("<==main");
    }
}
