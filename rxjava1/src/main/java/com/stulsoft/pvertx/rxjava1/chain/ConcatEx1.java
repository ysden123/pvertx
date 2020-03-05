/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.chain;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class ConcatEx1 {
    private static Logger logger = LoggerFactory.getLogger(ConcatEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var concatEx1 = new ConcatEx1();
        concatEx1.test1();

        logger.info("<==main");
    }

    void test1() {
        logger.info("==>test1");

        var counter = new CountDownLatch(1);
        var result = Single.concat(s1(), s2());

        result
                .doOnNext(s -> logger.info("s = {}", s))
                .doOnComplete(() -> {
                    logger.info("Completed");
                    counter.countDown();
                })
                .subscribe();

        try {
            counter.await(10, TimeUnit.SECONDS);
        } catch (Exception ignore) {
        }
        logger.info("<==test1");
    }

    Single<String> s1() {
        return Single.timer(5, TimeUnit.SECONDS)
                .map(l -> {
                    logger.info("Inside s1");
                    return "From s1";
                });
    }

    Single<String> s2() {
        return Single.timer(1, TimeUnit.SECONDS)
                .map(l -> {
                    logger.info("Inside s2");
                    return "From s2";
                });
    }
}
