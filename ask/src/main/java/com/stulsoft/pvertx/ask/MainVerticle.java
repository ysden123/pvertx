/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.ask;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class MainVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    public static void main(String[] args) {
        logger.info("==>main");


        var vertx = Vertx.vertx();

        vertx.deployVerticle(MainVerticle.class.getName(), mainDeployResult -> {
            if (mainDeployResult.succeeded()) {
                var jobRunner = new JobRunner(vertx);

                logger.info("Executing job ...");
                try {
                    var result = jobRunner.execute();
                    if (result != null) {
                        logger.info("Result: {}", result.encodePrettily());
                    } else {
                        logger.error("Result is null");
                    }
                } catch (Exception ex) {
                    logger.error("Failed to execute job. {}", ex.getMessage());
                }
            } else {
                logger.error("Failed to deploy MainVerticle. {}", mainDeployResult.cause().getMessage());
            }
        });

        logger.info("<==main");
    }

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Starting MainVerticle ...");
        logger.info("Deploying ServiceVerticle ...");
        vertx.deployVerticle(ServiceVerticle.class.getName(),
                new DeploymentOptions()
                        .setWorker(true)
                        .setInstances(5),
                deployResult -> {
                    if (deployResult.succeeded())
                        startFuture.complete();
                    else
                        startFuture.fail(deployResult.cause());
                });
    }
}
