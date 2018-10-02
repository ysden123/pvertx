/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.timeout;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Playing with timeout
 *
 * @author Yuriy Stul
 */
public class TimeoutEx1 {
    private static final Logger logger = LoggerFactory.getLogger(TimeoutEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        test2();

        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }
        logger.info("<==main");
    }

    /**
     * With timeout
     */
    private static void test1() {
        logger.info("==>test1");
        Single.just(1)
                .delay(200, TimeUnit.MILLISECONDS)
                .timeout(100, TimeUnit.MILLISECONDS, Single.error(new TimeoutException("TEST timeout")))
                .subscribe(i -> logger.info("i={}", i),
                        e -> logger.error(e.getMessage()));
        logger.info("<==test1");
    }

    /**
     * Without timeout
     */
    private static void test2() {
        logger.info("==>test2");
        Single.just(1)
                .timeout(100, TimeUnit.MILLISECONDS, Single.error(new TimeoutException("TEST timeout")))
                .subscribe(i -> logger.info("i={}", i),
                        e -> logger.error(e.getMessage()));
        logger.info("<==test2");
    }
}
