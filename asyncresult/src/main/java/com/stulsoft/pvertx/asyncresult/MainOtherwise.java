/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.asyncresult;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage the otherwise
 * <p>
 * The method otherwise is working only in case fail.
 *
 * @author Yuriy Stul
 */
public class MainOtherwise extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MainOtherwise.class);
    private static final String ADDRESS = "mainmap";
    private static final String COMMAND_SUCCESS = "success";
    private static final String COMMAND_FAIL = "fail";

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainOtherwise(), deployRes -> {
            vertx.eventBus().request(ADDRESS, COMMAND_SUCCESS, res -> resultHandler(COMMAND_SUCCESS, res));
            vertx.eventBus().request(ADDRESS, COMMAND_FAIL, res -> resultHandler(COMMAND_FAIL, res));
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
        process(message.body(), res ->
                message.reply(res.otherwise(t -> {
                    logger.info("in otherwise");
                    return "Hello from otherwise";
                }).result())
        );
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
