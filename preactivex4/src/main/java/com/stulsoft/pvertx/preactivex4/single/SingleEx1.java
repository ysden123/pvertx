/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.single;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing with Single
 *
 * @author Yuriy Stul
 */
public class SingleEx1 {
    private static final Logger logger = LoggerFactory.getLogger(SingleEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        testSuccess();
        testError();
        testFlatMap();
        testFlatMapWithError();
        logger.info("<==main");
    }

    private static void testSuccess() {
        logger.info("==>testSuccess");
        Single.just("one")
                .subscribe(s -> logger.info("Item [{}]", s),
                        t -> logger.error("Error: [{}]", t.getMessage()));
        logger.info("<==testSuccess");
    }

    private static void testError() {
        logger.info("==>testError");
        Single.error(new RuntimeException("test error"))
                .subscribe(s -> logger.info("Item [{}]", s),
                        t -> logger.error("Error: [{}]", t.getMessage()));
        logger.info("<==testError");
    }

    private static void testFlatMap() {
        logger.info("==>testFlatMap");
        Single.just("one")
                .flatMap(s -> Single.just(s.length()))
                .subscribe(s -> logger.info("Item [{}]", s),
                        t -> logger.error("Error: [{}]", t.getMessage()));
        logger.info("<==testFlatMap");
    }

    private static void testFlatMapWithError() {
        logger.info("==>testFlatMapWithError");
        Single.<String>error(new RuntimeException("test error"))
                .flatMap(s -> Single.just(s.length()))
                .subscribe(s -> logger.info("Item [{}]", s),
                        t -> logger.error("Error: [{}]", t.getMessage()));
        logger.info("<==testFlatMapWithError");
    }
}
