/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.deploy.options;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Main1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Main1.class);
    public static final String EB_ADDRESS = "main1";

    public static void main(String[] args) {
        logger.info("==>main");

        var config = ConfigBuilder.load("conf1.json");
        var deploymentOptions = new DeploymentOptions()
                .setWorker(true)
                .setConfig(config);
        logger.info("<==main");

        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.V1", deploymentOptions);
        vertx.deployVerticle("com.stulsoft.pvertx.deploy.options.Main1", deploymentOptions, depRes -> {
//            vertx.setPeriodic(500, l -> vertx.eventBus().send(EB_ADDRESS, "go!"));
            for (var i = 1; i <= 4; ++i) {
                vertx.eventBus().send(EB_ADDRESS, "go!");
            }
            vertx.setTimer(4000, l -> vertx.close());
        });
    }

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("==>start with  config: {}", config());
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startPromise.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void handler(Message<String> message) {
        logger.info("==>handler with message {}", message.body());
        vertx.eventBus().request(V1.EB_ADDRESS, "do it V1!", v1Res -> {
            if (v1Res.succeeded()) {
                logger.info("Response from V1: {}", v1Res.result().body().toString());
            } else {
                logger.error(v1Res.cause().getMessage(), v1Res.cause());
            }
        });
    }

}
