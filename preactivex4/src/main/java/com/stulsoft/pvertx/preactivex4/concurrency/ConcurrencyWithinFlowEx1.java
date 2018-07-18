/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.concurrency;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concurrency withing flow
 *
 * See <a href="https://github.com/ReactiveX/RxJava#concurrency-within-a-flow">Concurrency within a flow</a>
 *
 * @author Yuriy Stul
 */
public class ConcurrencyWithinFlowEx1 {
    private final static Logger logger = LoggerFactory.getLogger(ConcurrencyWithinFlowEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        test2();
        test3();
        logger.info("<==main");
    }

    /**
     * No concurrency in the computation step (v * v)
     */
    private static void test1() {
        logger.info("==>test1");
        Flowable.range(1, 3)
                .observeOn(Schedulers.computation())
                .map(v -> {
                    logger.info("in map v={}", v);
                    Thread.sleep(1000);
                    return v * v;
                })
                .blockingSubscribe(s -> logger.info("s={}", s));
        logger.info("<==test1");
    }

    /**
     * No concurrency in the computation step (v * v)
     */
    private static void test2() {
        logger.info("==>test2");
        Flowable.range(1, 3)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .map(v -> {
                    logger.info("in map v={}", v);
                    Thread.sleep(1000);
                    return v * v;
                })
                .blockingSubscribe(s -> logger.info("s={}", s));
        logger.info("<==test2");
    }

    /**
     * Concurrency in the computation step (w * w)
     */
    private static void test3() {
        logger.info("==>test3");
        Flowable.range(1, 3)
                .flatMap(v -> {
                    logger.info("in flotMap v={}", v);
                    return Flowable.just(v)
                            .subscribeOn(Schedulers.computation())
                            .map(w -> {
                                logger.info("in map w={}", w);
                                Thread.sleep(1000);
                                return w * w;
                            });
                })
                .blockingSubscribe(s -> logger.info("s={}", s));
        logger.info("<==test3");
    }

}
