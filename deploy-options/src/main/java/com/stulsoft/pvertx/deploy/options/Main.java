/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.deploy.options;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Main extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final String EB_ADDRESS = "main";

    public static void main(String[] args) {
        logger.info("==>main");
        var config = ConfigBuilder.load("conf1.json");

        var deploymentOptions = new DeploymentOptions()
                .setWorker(true)
                .setConfig(config);
        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.Main", deploymentOptions, depRes -> {
//            vertx.setPeriodic(500, l -> vertx.eventBus().send(EB_ADDRESS, "go!"));
            logger.info("vertx.deploymentIDs(): {}", vertx.deploymentIDs().toString());

            for (var i = 1; i <= 4; ++i) {
                vertx.eventBus().send(EB_ADDRESS, "go!");
            }
            vertx.setTimer(4000, l -> vertx.close());
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.info("==>start with  config: {}", config());
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    private void handler(Message<String> message) {
        logger.info("==>handler with message {}", message.body());
        vertx.eventBus().send(V1.EB_ADDRESS, "do it V1!", v1Res -> {
            if (v1Res.succeeded()) {
                logger.info("Response from V1: {}", v1Res.result().body().toString());
            } else {
                logger.error(v1Res.cause().getMessage(), v1Res.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
