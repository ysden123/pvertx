/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Runner1 {
    private static final Logger logger = LoggerFactory.getLogger(Runner1.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();

        var deploymentOptions = new DeploymentOptions()
                .setWorker(true);

        vertx.deployVerticle(
                MessageProcessorVerticle.class.getName(),
                deploymentOptions,
                dr -> {
                    var generator = new MessageGenerator(1000);
                    generator.generate().subscribe(s -> vertx
                            .eventBus()
                            .<Message<String>>request(
                                    MessageProcessorVerticle.EB_ADDRESS,
                                    s,
                                    ar ->{
//                                        logger.info("Result: {}", ar.result().body());
                                        logger.info("Result: {}", ar.result());
                                    }));
                });
    }
}
