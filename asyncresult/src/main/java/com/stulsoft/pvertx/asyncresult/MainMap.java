/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.asyncresult;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage the map
 *
 * @author Yuriy Stul
 */
public class MainMap extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MainMap.class);
    private static final String ADDRESS = "mainmap";
    private static final String COMMAND_SUCCESS = "success";
    private static final String COMMAND_FAIL = "fail";

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainMap(), deployRes -> {
            vertx.eventBus().send(ADDRESS, COMMAND_SUCCESS, res -> {
                if (res.succeeded())
                    logger.info("success: {} -> {}", COMMAND_SUCCESS, res.result().body());
                else
                    logger.info("fail: {} -> {}", COMMAND_SUCCESS, res.cause().getMessage());
            });

            vertx.eventBus().send(ADDRESS, COMMAND_FAIL, res -> {
                if (res.succeeded())
                    logger.info("success: {} -> {}", COMMAND_FAIL, res.result().body());
                else
                    logger.info("fail: {} -> {}", COMMAND_FAIL, res.cause().getMessage());
            });
        });


        vertx.setTimer(2000, l -> vertx.close());
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(ADDRESS, this::handler);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void handler(final Message<String> message) {
        process(message.body(), res -> {
            if (res.succeeded()) {
                logger.info("(1) command={}, result={}", message.body(), res.map(s -> s.equals("success")).result());
                message.reply(res.map(s -> s.equals("success")).result());
            } else {
                logger.info("(2) command={}, result={}", message.body(), res.map(s -> s.equals("success")).result());
                message.fail(123, res.cause().getMessage());
            }
        });
    }

    private void process(final String command, Handler<AsyncResult<String>> handler) {
        switch (command) {
            case COMMAND_SUCCESS:
                handler.handle(Future.succeededFuture("success"));
                break;
            case COMMAND_FAIL:
                handler.handle(Future.failedFuture(new RuntimeException("failure")));
                break;
        }
    }
}
