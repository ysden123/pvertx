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
 * <p>
 * The method map is working only in case success.
 * In case failure the method returns <i>null</i>
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
            vertx.eventBus().send(ADDRESS, COMMAND_SUCCESS, res -> resultHandler(COMMAND_SUCCESS, res));
            vertx.eventBus().send(ADDRESS, COMMAND_FAIL, res -> resultHandler(COMMAND_FAIL, res));
        });

        vertx.setTimer(2000, l -> vertx.close());
    }

    /**
     * Handles a result of work
     *
     * @param command     the command
     * @param asyncResult result
     */
    private static void resultHandler(final String command, final AsyncResult<Message<Object>> asyncResult) {
        if (asyncResult.succeeded())
            logger.info("Succeeded. command: {} -> result: {}", command, asyncResult.result().body());
        else
            logger.info("Failed. command: {} -> error: {}", command, asyncResult.cause().getMessage());
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(ADDRESS, this::messageHandler);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    /**
     * Handles a event message
     *
     * @param message the message
     */
    private void messageHandler(final Message<String> message) {
        process(message.body(), res -> {
            logger.info("Result of map: {}", res.map(s -> {
                logger.info("in map");
                return s.equals("success");
            }));
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
