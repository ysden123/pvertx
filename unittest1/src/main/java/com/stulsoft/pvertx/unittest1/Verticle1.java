package com.stulsoft.pvertx.unittest1;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/6/2018
 */
public class Verticle1 extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(Verticle1.class);

    @Override
    public void start() throws Exception {
        logger.info("Starting Verticle1...");
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping Verticle1...");
        super.stop();
    }
}
