/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.autoclose;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing with Vertx
 * <p>
 * Example of <i>"auto"</i> closing the Vertx at the end of execution.
 *
 * @author Yuriy Stul
 */
public class AutoCloseMain extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(AutoCloseMain.class);
    private static final String ADDRESS_START = "auto_close_main_start";
    private static final String ADDRESS_STOP = "auto_close_main_stop";
    private static final String COMMAND_START = "start";
    private static final String COMMAND_STOP = "stop";

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AutoCloseMain(), deployRes ->
                vertx.eventBus().request(ADDRESS_START, COMMAND_START, startResult -> {
                    logger.info("Start result: {}", startResult.result().body().toString());
                    vertx.eventBus().send(ADDRESS_STOP, COMMAND_STOP);
                })
        );
        logger.info("<==main");
    }

    @Override
    public void start() throws Exception {
        logger.info("Starting...");
        vertx.eventBus().consumer(ADDRESS_START, this::handler);
        vertx.eventBus().consumer(ADDRESS_STOP, m -> vertx.close());
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping...");
        super.stop();
    }

    private void handler(Message<String> message) {
        logger.info("message is {}", message.body());
        message.reply("Done");
    }
}
