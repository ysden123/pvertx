package com.stulsoft.pvertx.httpserver2.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/10/2018
 */
public class Service2 extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(Service2.class);

    @Override
    public void start() throws Exception {
        logger.info("Starting Service2...");
        vertx.eventBus().<String>consumer("service2Address", handler -> handler.reply("reply from service2"));
        super.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
