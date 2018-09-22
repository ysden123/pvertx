/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.operator;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing with map and flatMap operators.
 *
 * @author Yuriy Stul
 */
public class MapEx01 {
    private static Logger logger = LoggerFactory.getLogger(MapEx01.class);

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
        Observable.just(1, 2, 3)
                .map(i -> i + 1000)
                .subscribe(r -> logger.info("r = {}", r));

        logger.info("<==test1");
    }

    private static void test2() {
        logger.info("==>test2");
        Observable.just(1, 2, 3)
                .flatMap(i -> Observable.just(i + 1000))
                .subscribe(r -> logger.info("r = {}", r));

        logger.info("<==test2");
    }

    private static void test3() {
        logger.info("==>test3");
        Single.just(1)
                .map(i -> Single.just(i + 1000))
                .subscribe(r -> {
                    logger.info("(1) r = {}", r);
                    logger.info("(2) r.blockingGet() = {}", r.blockingGet());
                    r.subscribe(i -> logger.info("(3) r = {}", i));
                });

        logger.info("<==test3");
    }

    private static void test4() {
        logger.info("==>test4");
        Single.just(1)
                .flatMap(i -> Single.just(i + 1000))
                .subscribe(r -> logger.info("r = {}", r));

        logger.info("<==test4");
    }
}
