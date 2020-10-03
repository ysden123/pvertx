/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Yuriy Stul
 */
public class ChainDeployment2 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ChainDeployment2.class);

    public static void main(String[] args) {
        var vertx = Vertx.vertx();

        vertx.deployVerticle("com.stulsoft.pvertx.pfuture2.ChainDeployment", result -> {
            if (result.succeeded())
                logger.info("Total success");
            else
                logger.error("Total failure {}", result.cause().getMessage());
        });
    }

    @Override
    public void start(Promise<Void> startPromise){
        logger.info("==>start");

        var deploys = new ArrayList<Future>();

        deploys.add(deploy("com.stulsoft.pvertx.pfuture2.V1"));
        deploys.add(deploy("com.stulsoft.pvertx.pfuture2.V2"));

        CompositeFuture.all(deploys).onComplete(deployResult -> {
            if (deployResult.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(deployResult.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private Future<AsyncResult<String>> deploy(final String verticleName) {
        Promise<AsyncResult<String>> promise = Promise.promise();
        vertx.deployVerticle(verticleName, deployResult -> {
            if (deployResult.succeeded())
                promise.complete(deployResult);
            else
                promise.fail(deployResult.cause().getMessage());
        });
        return promise.future();
    }
}
