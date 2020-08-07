/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiinst;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks handling send, request, and publish request in multiple service (Verticle) instances
 * <p>
 * Usage of compose
 * </p>
 *
 * @author Yuriy Stul
 */
public class MultiInstRunnerComposite {
    private static final Logger logger = LoggerFactory.getLogger(MultiInstRunnerComposite.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        var deploymentOptions = new DeploymentOptions()
                .setInstances(2);

        vertx.deployVerticle(ServiceForMultiInst.class.getName(), deploymentOptions, deployAr -> {
            if (deployAr.succeeded()) {
                logger.info("Deployed successfully");
                try {
                    send(vertx)
                            .compose(v -> request(vertx))
                            .compose(v -> publish(vertx))
                            .onComplete(ar -> {
                                logger.info("<==main");
                                vertx.close();
                            });
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    vertx.close();
                    logger.info("<==main");
                }
            } else {
                logger.error("Failed deployment: {}", deployAr.cause().getMessage());
            }
        });
    }

    private static Future<Void> send(final Vertx vertx) {
        logger.info("==>send");
        var promise = Promise.<Void>promise();
        vertx.eventBus()
                .send(ServiceForMultiInst.EB_ADDRESS, "do it send 1")
                .send(ServiceForMultiInst.EB_ADDRESS, "do it send 2");
        vertx.setTimer(1000, l -> promise.complete());
        return promise.future();
    }

    private static Future<Void> request(final Vertx vertx) {
        logger.info("==>request");
        var promise = Promise.<Void>promise();
        vertx.eventBus()
                .request(ServiceForMultiInst.EB_ADDRESS, "do it request 1", ar -> {
                    logger.info("request result: {}", ar.result().body());
                })
                .request(ServiceForMultiInst.EB_ADDRESS, "do it request 2", ar -> {
                    logger.info("request result: {}", ar.result().body());
                });
        vertx.setTimer(1000, l -> promise.complete());
        return promise.future();
    }

    private static Future<Void> publish(final Vertx vertx) {
        logger.info("==>publish");
        var promise = Promise.<Void>promise();
        vertx.eventBus().publish(ServiceForMultiInst.EB_ADDRESS, "do it publish");
        vertx.setTimer(1000, l -> promise.complete());
        return promise.future();
    }
}
