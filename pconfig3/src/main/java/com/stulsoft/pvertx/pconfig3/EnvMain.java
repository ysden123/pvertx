/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.pconfig3;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.stulsoft.pvertx.pconfig3.Utils.showConfig;

/**
 * Shows environment configuration
 *
 * @author Yuriy Stul
 */
public class EnvMain {
    private static Logger logger = LoggerFactory.getLogger(EnvMain.class);

    public static void main(String[] args) {
        logger.info("==>main");

        Vertx vertx = Vertx.vertx();
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("env");

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

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
