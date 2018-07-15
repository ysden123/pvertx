/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Completable with chain
 *
 * @author Yuriy Stul
 */
public class CompletableEx2 {
    private static final Logger logger = LoggerFactory.getLogger(CompletableEx2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var totalRes = success().andThen(success2());
        totalRes.subscribe(() -> logger.info("(1) Total success"),
                t -> logger.error("(1) Total error {}", t.getMessage()));

        totalRes = success()
                .andThen(error())
                .andThen(success2());
        totalRes.subscribe(() -> logger.info("(2) Total success"),
                t -> logger.error("(2) Total error {}", t.getMessage()));
        logger.info("<==main");
    }

    private static Completable success() {
        logger.info("==>success");
        return Completable.complete();
    }

    private static Completable success2() {
        logger.info("==>success2");
        return Completable.complete();
    }

    private static Completable error() {
        logger.info("==>error");
        return Completable.error(new RuntimeException("test error"));
    }
}
