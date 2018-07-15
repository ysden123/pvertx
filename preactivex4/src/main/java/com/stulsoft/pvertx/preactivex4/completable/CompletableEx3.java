/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Completable with chain
 *
 * @author Yuriy Stul
 */
public class CompletableEx3 {
    private static final Logger logger = LoggerFactory.getLogger(CompletableEx3.class);

    public static void main(String[] args) {
        logger.info("==>main");

        andThenEx1();
        andThenWithErrorEx1();
        concatEx1();
        concatWithErrorEx1();

        logger.info("<==main");
    }

    private static Completable success1() {
        logger.info("==>success1");
        return Completable.timer(1, TimeUnit.SECONDS);
    }

    private static Completable success2() {
        logger.info("==>success2");
        return Completable.timer(1, TimeUnit.SECONDS);
    }

    private static Completable error1() {
        logger.info("==>error1");
        return Completable
                .timer(2, TimeUnit.SECONDS)
                .andThen(Completable.error(new RuntimeException("test error")));
    }

    private static void andThenEx1() {
        logger.info("==>andThenEx1");
        var totalResult = success1()
                .andThen(Completable.defer(CompletableEx3::success2));
        var d = totalResult.subscribe(() -> logger.info("Total completed"));
        totalResult.blockingAwait();
        d.dispose();
        logger.info("<==andThenEx1");
    }

    private static void andThenWithErrorEx1() {
        logger.info("==>andThenWithErrorEx1");
        var totalResult = success1()
                .andThen(Completable.defer(CompletableEx3::error1))
                .andThen(Completable.defer(CompletableEx3::success2));
        var d = totalResult.subscribe(() -> logger.info("(1) Total completed"),
                t -> logger.error("(1) Total error: {}", t.getMessage()));
        try {
            totalResult.blockingAwait();
        } catch (Exception ex) {
            logger.error("(2) Total error: {}", ex.getMessage());
        }
        d.dispose();

        logger.info("<==andThenWithErrorEx1");
    }

    private static void concatEx1() {
        logger.info("==>concatEx1");
        var totalResult = Completable.concatArray(success1(), success2());
        var d = totalResult.subscribe(() -> logger.info("(1) Total completed"),
                t -> logger.error("(1) Total error: {}", t.getMessage()));
        totalResult.blockingAwait();
        d.dispose();
        logger.info("<==concatEx1");
    }

    private static void concatWithErrorEx1() {
        logger.info("==>concatWithErrorEx1");
        var totalResult = Completable.concatArray(success1(), error1(), success2());
        var d = totalResult.subscribe(() -> logger.info("(1) Total completed"),
                t -> logger.error("(1) Total error: {}", t.getMessage()));
        try {
            totalResult.blockingAwait();
        } catch (Exception ex) {
            logger.error("(2) Total error: {}", ex.getMessage());
        }
        d.dispose();
        logger.info("<==concatWithErrorEx1");
    }
}
