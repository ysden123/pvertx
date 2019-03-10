/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pconfig2;

import com.stulsoft.pvertx.common.ConfigRetrieverFactory;
import com.stulsoft.pvertx.common.Terminator;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Started Main");

        Vertx vertx = Vertx.vertx();
        Verticle1 v1 = new Verticle1();


        var configRetriever = ConfigRetrieverFactory.configRetriever(vertx, "conf/config.json");
        configRetriever.getConfig(ar -> {
            if (ar.failed()) {
                logger.error("Failed getting configuration. {}", ar.cause().getMessage());
            } else {
                DeploymentOptions deploymentOptions = new DeploymentOptions()
                        .setConfig(ar.result());
                vertx.deployVerticle(v1, deploymentOptions);
            }
        });

        Terminator.terminate(vertx);
        logger.info("Completed Main");
    }
}
