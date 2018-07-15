/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.maybe;

import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing with Maybe.
 * <p>
 * See <a href="http://www.baeldung.com/rxjava-maybe">RxJava Maybe</a>
 *
 * @author Yuriy Stul
 */
public class MaybeEx1 {
    private static final Logger logger = LoggerFactory.getLogger(MaybeEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        test2();
        test3();
        test4();
        logger.info("<==main");
    }

    private static void test1() {
        logger.info("==>test1");
        Maybe.just(1)
                .map(x -> x + 7)
                .filter(x -> x > 0)
                .test()
                .assertResult(8);
        logger.info("<==test1");
    }

    private static void test2() {
        logger.info("==>test2");
        Maybe.just(1)
                .subscribe(
                        x -> logger.info("Emitted item: {}", x),
                        ex -> logger.error("Error: {}", ex.getMessage()),
                        () -> logger.info("Completed. No items.")
                );
        logger.info("<==test2");
    }

    private static void test3() {
        logger.info("==>test3");
        Maybe.empty()
                .subscribe(
                        x -> logger.info("Emitted item: {}", x),
                        ex -> logger.error("Error: {}", ex.getMessage()),
                        () -> logger.info("Completed. No items.")
                );
        logger.info("<==test3");
    }

    private static void test4() {
        logger.info("==>test4");
        Maybe.error(new RuntimeException("test error"))
                .subscribe(
                        x -> logger.info("Emitted item: {}", x),
                        ex -> logger.error("Error: {}", ex.getMessage()),
                        () -> logger.info("Completed. No items.")
                );
        logger.info("<==test4");
    }
}
