/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.single;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SingleEx1 {
    private static final Logger logger = LoggerFactory.getLogger(SingleEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var s = new SingleEx1();

        s.test1();
        s.test2();

        logger.info("<==main");
    }

    /**
     * Returns 1 string
     */
    private void test1() {
        logger.info("==>test1");
        var single = Single.just("Hello");

        single.subscribe(s -> logger.info(s));
        logger.info("<==test1");
    }

    /**
     * Returns exception - error
     */
    private void test2() {
        logger.info("==>test2");
        var error = Single.<String>error(new RuntimeException("Test error"));

        error
                .observeOn(Schedulers.io())
                .subscribe(
                        s -> logger.info(s),
                        e -> logger.error(e.getMessage())
                );
        logger.info("<==test2");
    }
}
