/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage of Future compose
 *
 * @author Yuriy Stul
 */
public class CompositeMain1 {
    private static final Logger logger = LoggerFactory.getLogger(CompositeMain1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        f1(vertx)
                .compose(v -> f2(vertx))
                .compose(v -> end(vertx));
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

    private static Future<Void> end(final Vertx vertx) {
        logger.info("==>end");
        Promise<Void> promise = Promise.promise();
        vertx.close();
        promise.complete();
        return promise.future();
    }
}
