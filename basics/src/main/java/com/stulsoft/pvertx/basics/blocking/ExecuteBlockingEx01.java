/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import io.reactivex.Single;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One action
 *
 * @author Yuriy Stul
 */
public class ExecuteBlockingEx01 {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteBlockingEx01.class);
    private final Vertx vertx;

    ExecuteBlockingEx01(final Vertx vertx) {
        this.vertx = vertx;
    }

    // Incorrect usage
    Single<String> test1() {
        logger.info("==>test1");
        return Single.create(source -> {
            vertx.executeBlocking(handler -> {
                logger.info("Start timer");
                vertx.setTimer(1000, l -> {
                    logger.info("In process...");
                    source.onSuccess("Done 1");
                });
            }, ar -> {
                if (ar.succeeded()) {
                    logger.info("Succeeded");
                    source.onSuccess("Done 2");
                } else {
                    logger.error("Failed: {}", ar.cause().getMessage());
                    source.onError(new RuntimeException(ar.cause().getMessage()));
                }
            });
        });
    }

    // Correct usage
    Single<String> test2() {
        logger.info("==>test2");
        return Single.create(source -> {
            vertx.executeBlocking(future -> {
                logger.info("Start timer");
                vertx.setTimer(1000, l -> {
                    logger.info("In process...");
                    future.complete("Done 2");
                });
            }, result -> {
                if (result.succeeded()) {
                    logger.info("Succeeded");
                    source.onSuccess(result.result().toString());
                } else {
                    logger.error("Failed: {}", result.cause().getMessage());
                    source.onError(new RuntimeException(result.cause().getMessage()));
                }
            });
        });
    }

    Single<String> test2WithError() {
        logger.info("==>test2WithError");
        return Single.create(source -> {
            vertx.executeBlocking(future -> {
                logger.info("Start timer");
                vertx.setTimer(1000, l -> {
                    logger.info("In process...");
                    future.fail(new RuntimeException("Test exception"));
                });
            }, result -> {
                if (result.succeeded()) {
                    logger.info("Succeeded");
                    source.onSuccess(result.result().toString());
                } else {
                    logger.error("Failed: {}", result.cause().getMessage());
                    source.onError(new RuntimeException(result.cause().getMessage()));
                }
            });
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var instance = new ExecuteBlockingEx01(vertx);

/*
        instance.test1().subscribe(rslt -> {
            logger.info("Result: {}", rslt);
            vertx.close();
            logger.info("<==main");
        }, err -> {
            vertx.close();
            logger.error("Error 1: {}", err.getMessage());
        });
*/

/*
        instance.test2().subscribe(rslt -> {
            logger.info("Result: {}", rslt);
            vertx.close();
            logger.info("<==main");
        }, err -> {
            vertx.close();
            logger.error("Error 2: {}", err.getMessage());
        });
*/

        instance.test2WithError().subscribe(rslt -> {
            logger.info("Result: {}", rslt);
            vertx.close();
            logger.info("<==main");
        }, err -> {
            vertx.close();
            logger.error("Error 2: {}", err.getMessage());
        });
    }
}
