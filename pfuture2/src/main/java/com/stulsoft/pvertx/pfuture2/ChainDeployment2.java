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
    public void start(Future<Void> startFuture) throws Exception {
        logger.info("==>start");

        var deploys = new ArrayList<Future>();

        deploys.add(deploy("com.stulsoft.pvertx.pfuture2.V1"));
        deploys.add(deploy("com.stulsoft.pvertx.pfuture2.V2"));

        CompositeFuture.all(deploys).setHandler(deployResult -> {
            if (deployResult.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(deployResult.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private Future<AsyncResult> deploy(final String verticleName) {
        Future<AsyncResult> result = Future.future();
        vertx.deployVerticle(verticleName, deployResult -> {
            if (deployResult.succeeded())
                result.complete(deployResult);
            else
                result.fail(deployResult.cause().getMessage());
        });
        return result;
    }
}
