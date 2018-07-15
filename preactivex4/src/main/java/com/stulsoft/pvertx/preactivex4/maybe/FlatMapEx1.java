/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.maybe;

import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing with flatMap
 *
 * @author Yuriy Stul
 */
public class FlatMapEx1 {
    private static final Logger logger = LoggerFactory.getLogger(FlatMapEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        testNotEmpty();
        testEmpty();
        testError();
        logger.info("<==main");
    }

    private static void testNotEmpty() {
        logger.info("==>testNotEmpty");
        Maybe.just("One")
                .flatMap(s -> Maybe.just(s.length()))
                .subscribe(
                        x -> logger.info("Emitted item: {}", x),
                        ex -> logger.error("Error: {}", ex.getMessage()),
                        () -> logger.info("Completed. No items.")
                );
        logger.info("<==testNotEmpty");
    }

    private static void testEmpty() {
        logger.info("==>testEmpty");
        Maybe.<String>empty()
                .flatMap(s -> Maybe.just(s.length()))
                .subscribe(
                        x -> logger.info("Emitted item: {}", x),
                        ex -> logger.error("Error: {}", ex.getMessage()),
                        () -> logger.info("Completed. No items.")
                );
        logger.info("<==testEmpty");
    }

    private static void testError() {
        logger.info("==>testError");
        Maybe.<String>error(new RuntimeException("test error"))
                .flatMap(s -> Maybe.just(s.length()))
                .subscribe(
                        x -> logger.info("Emitted item: {}", x),
                        ex -> logger.error("Error: {}", ex.getMessage()),
                        () -> logger.info("Completed. No items.")
                );
        logger.info("<==testError");
    }

}
