/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ChainDeployment extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ChainDeployment.class);

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
        var deployResult = Future.<AsyncResult>future();

        deployResult.setHandler(r -> {
            if (r.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(r.cause());
            }
            vertx.close();
        });

        deploy("com.stulsoft.pvertx.pfuture2.V1")
                .compose(r -> deploy("com.stulsoft.pvertx.pfuture2.V2"))
                .compose(r -> deployResult.complete(), deployResult);
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
