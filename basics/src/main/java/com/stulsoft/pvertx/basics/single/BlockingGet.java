/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.single;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Use of the blockingGet
 *
 * @author Yuriy Stul
 */
public class BlockingGet {
    private static final Logger logger = LoggerFactory.getLogger(BlockingGet.class);

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        logger.info("<==main");
    }

    private static void test1() {
        logger.info("==>test1");
        var list = Observable
                .range(1, 3)
                .concatMap(x -> Observable.just(x, -x))
                .map(Objects::toString)
                .toList()
                .blockingGet();
        logger.info("list: {}", list);
        logger.info("<==test1");
    }
}
