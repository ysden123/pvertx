/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.shared;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.DeploymentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Runner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        var deploymentOptions = new DeploymentOptions()
                .setInstances(4);
        vertx.deployVerticle(ServiceVerticle.class.getName(), deploymentOptions,
                dr -> vertx.setPeriodic(1500L, l ->
                        vertx.eventBus().<String>request(ServiceVerticle.EB_ADDRESS, "test",
                                ar -> logger.info("Response: {}", ar.result().body())))
        );
    }
}
