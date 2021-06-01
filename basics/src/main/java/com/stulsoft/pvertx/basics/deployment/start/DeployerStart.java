/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.deployment.start;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class DeployerStart {
    private static final Logger logger = LoggerFactory.getLogger(DeployerStart.class);

    public static void main(String[] args) {
        var vertx = Utils.createVertx();

        vertx.deployVerticle(Verticle1.class.getName())
                .compose(__ -> vertx.deployVerticle(Verticle2.class.getName()))
                .onComplete(__ -> {
                    logger.info("Starting the system");
                    vertx.eventBus().publish("START", "Start");

                    vertx.setTimer(2000, ___ -> vertx.close());
                });
    }
}
