/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.pconfig3;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.stulsoft.pvertx.pconfig3.Utils.showConfig;

/**
 * Shows all configurations: sys, env, and conf/config.json
 *
 * @author Yuriy Stul
 */
public class DefaultJsonMain {
    private static Logger logger = LoggerFactory.getLogger(DefaultJsonMain.class);

    public static void main(String[] args) {
        logger.info("==>main");

        Vertx vertx = Vertx.vertx();

        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        retriever.getConfig(configResult -> {
            if (configResult.succeeded()) {
                logger.info("Received configuration with {} fields", configResult.result().fieldNames().size());
                showConfig(configResult.result());
            } else {
                logger.error("Failed receiving configuration. Error {}", configResult.cause().getMessage());
            }
            vertx.close();
            logger.info("<==main");
        });
    }
}
