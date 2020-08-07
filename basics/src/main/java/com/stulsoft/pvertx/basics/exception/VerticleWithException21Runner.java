/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.exception;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class VerticleWithException21Runner {
    private static final Logger logger = LoggerFactory.getLogger(VerticleWithException21Runner.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.deployVerticle(VerticleWithException2.class.getName(),
                dr -> callService(vertx, "test1")
                        .compose(v -> callService(vertx, "test2"))
                        .setHandler(ar -> {
                            if (ar.failed()) {
                                logger.error("Total failure: {}", ar.cause().getMessage());
                            }
                            vertx.close();
                            logger.info("<==main");
                        })
        );
    }

    private static Future<Void> callService(Vertx vertx, String msg) {
        var promise = Promise.<Void>promise();
        vertx
                .eventBus()
                .request(VerticleWithException2.EB_ADDRESS,
                        msg,
                        ar -> promise.complete());
        return promise.future();
    }
}
