package com.stulsoft.pvertx.pconfig2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
        vertx.setTimer(100, (l) -> showConfig());
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping Verticle1...");
        super.stop();
    }

    private void showConfig() {
        logger.info("==>showConfig");
        logger.info("fields:");
        config().forEach(s -> logger.info("field: {}", s));
        String param1 = config().getString("param1");
        logger.info("param1: {}", param1);
        JsonArray ports = config().getJsonArray("ports");
        logger.info("ports:");
        ports.forEach(c -> logger.info("{}", ((JsonObject) c).getInteger("port")));
        logger.info("<==showConfig");
    }
}
