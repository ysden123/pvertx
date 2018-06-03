/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage of Future compose
 *
 * @author Yuriy Stul
 */
public class CompositeMain3 {
    private static final Logger logger = LoggerFactory.getLogger(CompositeMain3.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        Future<Void> startFuture = Future.future();
        startFuture.setHandler(rs -> {
            logger.info("Close vertx");
            vertx.close();
        });
        f1(vertx)
                .compose(v -> f2(vertx))
                .compose(v -> startFuture.complete(), startFuture);
    }

    private static Future<Void> f1(final Vertx vertx) {
        logger.info("==>f1");
        Future<Void> future = Future.future();
        vertx.setTimer(100, l -> {
            logger.info("Completed f1");
            future.complete();
        });
        return future;
    }

    private static Future<Void> f2(final Vertx vertx) {
        logger.info("==>f2");
        Future<Void> future = Future.future();
        vertx.setTimer(100, l -> {
            logger.info("Completed f2");
            future.complete();
        });
        return future;
    }

}
