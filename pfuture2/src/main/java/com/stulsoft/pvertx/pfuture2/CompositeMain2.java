/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage of Future compose
 *
 * @author Yuriy Stul
 */
public class CompositeMain2 {
    private static final Logger logger = LoggerFactory.getLogger(CompositeMain2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        execAll(vertx, res -> {
            if (res.succeeded()) {
                logger.info("Success");
            } else {
                logger.info("Failure");
            }
            vertx.close();
        });

    }

    private static void execAll(final Vertx vertx, final Handler<AsyncResult<Void>> handler) {
        logger.info("==>execAll");
        Promise<Void> promise = Promise.promise();
        promise.future().onComplete(handler);
        f1(vertx)
                .compose(v -> f2(vertx))
                .compose(v -> {
                    promise.complete();
                    return promise.future();
                }, err -> {
                    promise.fail(err);
                    return promise.future();
                });
    }

    private static Future<Void> f1(final Vertx vertx) {
        logger.info("==>f1");
        Promise<Void> promise = Promise.promise();
        vertx.setTimer(100, l -> {
            logger.info("Completed f1");
            promise.complete();
        });
        return promise.future();
    }

    private static Future<Void> f2(final Vertx vertx) {
        logger.info("==>f2");
        Promise<Void> promise = Promise.promise();
        vertx.setTimer(100, l -> {
            logger.info("Completed f2");
            promise.complete();
        });
        return promise.future();
    }

}
