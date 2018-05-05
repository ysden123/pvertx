/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pconfig2;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Started Main");

        Vertx vertx = Vertx.vertx();
        Verticle1 v1 = new Verticle1();

        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "conf/config.json"));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.getConfig(ar -> {
            if (ar.failed()) {
                logger.error("Failed getting configuration. {}", ar.cause().getMessage());
            } else {
                DeploymentOptions deploymentOptions = new DeploymentOptions()
                        .setConfig(ar.result());
                vertx.deployVerticle(v1, deploymentOptions);
            }
        });

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();

        vertx.close();
        logger.info("Completed Main");
    }
}
