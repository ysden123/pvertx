/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usage the recover of the Future
 *
 * @author Yuriy Stul
 */
public class RecoverMain {
    private static final Logger logger = LoggerFactory.getLogger(RecoverMain.class);

    public static void main(String[] args) {
        logger.info("==>main");

        succeededFuture()
                .recover(RecoverMain::recoverFuture)
                .onComplete(r -> logger.info("Result without recover {}", r.result()));
        failedFuture()
                .recover(RecoverMain::recoverFuture)
                .onComplete(r -> logger.info("Result with recover {}", r.result()));
        logger.info("<==main");
    }

    private static Future<String> succeededFuture() {
        return Future.succeededFuture("test success");
    }

    private static Future<String> failedFuture() {
        return Future.failedFuture("test error");
    }

    private static Future<String> recoverFuture(Throwable t) {
        return Future.succeededFuture("success text for exception " + t.getMessage());
    }
}
