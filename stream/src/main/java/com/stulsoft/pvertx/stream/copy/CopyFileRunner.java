/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class CopyFileRunner {
    private final static Logger logger = LoggerFactory.getLogger(CopyFileRunner.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(CopyFileVerticle.class.getName(), deployResult -> {
            if (deployResult.failed()) {
                logger.error("Failed deploying [{}], error: {}",
                        CopyFileVerticle.class.getName(),
                        deployResult.cause().getMessage());
                vertx.close();
            } else {
                vertx.eventBus().send(CopyFileVerticle.EB_ADDRESS,
                        new JsonObject()
                                .put("srcFileName", "text1.txt")
                                .put("dstFileName", "text1Copy.txt"),
                        execResult -> {
                            if (execResult.failed()) {
                                logger.error("Failed copying file. Error: {}", execResult.cause().getMessage());
                            } else {
                                logger.info("Result of copying is {}", execResult.result().body());
                            }
                            vertx.close();
                        });
            }
        });
        logger.info("<==main");
    }
}
