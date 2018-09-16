/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.maybe;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Maybe.
 *
 * @author Yuriy Stul
 */
public class MaybeSimple {
    private static Logger logger = LoggerFactory.getLogger(MaybeSimple.class);

    public static void main(String[] args) {
        logger.info("==>main");
        success();
        empty();
        error();
        logger.info("<==main");
    }

    /**
     * Success
     */
    private static void success() {
        logger.info("==>success");
        handle(Maybe.just(1)).dispose();
        logger.info("<==success");
    }

    /**
     * Empty
     */
    private static void empty() {
        logger.info("==>empty");
        handle(Maybe.empty()).dispose();
        logger.info("<==empty");
    }

    /**
     * Error
     */
    private static void error() {
        logger.info("==>error");
        handle(Maybe.error(new RuntimeException("test error"))).dispose();
        logger.info("<==error");
    }

    private static Disposable handle(Maybe<Integer> mayBe) {
        return mayBe.subscribe(i -> logger.info("success: i = {}", i),
                e -> logger.error("error: e = {}", e.getMessage()),
                () -> logger.info("complete"));
    }
}
