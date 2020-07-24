/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiinst;

import com.stulsoft.pvertx.basics.Utils;
import io.reactivex.Completable;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks handling send, request, and publish request in multiple service (Verticle) instances
 * <p>
 * Usage of andThen
 * </p>
 *
 * @author Yuriy Stul
 */
public class MultiInstRunnerComposite2 {
    private static final Logger logger = LoggerFactory.getLogger(MultiInstRunnerComposite2.class);

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
                            .andThen(request(vertx))
                            .andThen(publish(vertx))
                            .subscribe(() -> {
                                vertx.close();
                                logger.info("<==main");
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

    private static Completable send(final Vertx vertx) {
        logger.info("==>send");
        return Completable.create(source -> {
            vertx.eventBus()
                    .send(ServiceForMultiInst.EB_ADDRESS, "do it send 1")
                    .send(ServiceForMultiInst.EB_ADDRESS, "do it send 2");
            vertx.setTimer(1000, l -> source.onComplete());
        });
    }

    private static Completable request(final Vertx vertx) {
        logger.info("==>request");
        return Completable.create(source -> {
            vertx.eventBus()
                    .request(ServiceForMultiInst.EB_ADDRESS, "do it request 1", ar -> {
                        logger.info("request result: {}", ar.result().body());
                    })
                    .request(ServiceForMultiInst.EB_ADDRESS, "do it request 2", ar -> {
                        logger.info("request result: {}", ar.result().body());
                    });
            vertx.setTimer(1000, l -> source.onComplete());
        });
    }

    private static Completable publish(final Vertx vertx) {
        logger.info("==>publish");
        return Completable.create(source -> {
            vertx.eventBus()
                    .publish(ServiceForMultiInst.EB_ADDRESS, "do it publish");
            vertx.setTimer(1000, l -> source.onComplete());
        });
    }
}
