/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pchain;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Application3 {
    private static final Logger logger = LoggerFactory.getLogger(Application3.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();
        String[] verticles = new String[]{ServiceVerticleWithError.class.getName(), ChainMngr3Verticle.class.getName()};
        Utils.deployVerticles(vertx, verticles)
                .subscribe(
                        () -> vertx.eventBus().request(
                                ChainMngr3Verticle.EB_ADDRESS,
                                "start",
                                ar -> {
                                    if (ar.succeeded()) {
                                        logger.info("Succeeded all jobs. {}", ar.result().body().toString());
                                    } else {
                                        logger.error("Failed a job. {}", ar.cause().getMessage());
                                    }
                                    vertx.close();
                                })
                );
    }
}
