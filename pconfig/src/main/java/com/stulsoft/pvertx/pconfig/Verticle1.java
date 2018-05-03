package com.stulsoft.pvertx.pconfig;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
public class Verticle1 extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(Verticle1.class);

    @Override
    public void start() throws Exception {
        logger.info("Starting Verticle1...");
        config().fieldNames().forEach(s -> logger.info("field: {}", s));
        String param1 = config().getString("param1");
        logger.info("param1: {}", param1);
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping Verticle1...");
        super.stop();
    }
}
