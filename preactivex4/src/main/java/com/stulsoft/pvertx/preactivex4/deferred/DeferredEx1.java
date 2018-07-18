/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.deferred;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Playing with deferred
 * <p>
 * See <a href="https://github.com/ReactiveX/RxJava#deferred-dependent">Deferred-dependent</a>
 *
 * @author Yuriy Stul
 */
public class DeferredEx1 {
    private static final Logger logger = LoggerFactory.getLogger(DeferredEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        test2();
        logger.info("<==main");
    }

    /**
     * Single.just(count.get()) is running at assembly time only!
     */
    private static void test1() {
        logger.info("==>test1");
        AtomicInteger count = new AtomicInteger();

        Observable.range(1, 3)
                .doOnNext(ignored -> count.incrementAndGet())
                .ignoreElements()
                .andThen(Single.just(count.get()))
                .subscribe(r -> logger.info("{}", r));
        logger.info("<==test1");
    }

    /**
     * Single.just(count.get()) is running at runtime
     */
    private static void test2() {
        logger.info("==>test2");
        AtomicInteger count = new AtomicInteger();

        Observable.range(1, 3)
                .doOnNext(ignored -> count.incrementAndGet())
                .ignoreElements()
                .andThen(Single.defer(() -> Single.just(count.get())))
                .subscribe(r -> logger.info("{}", r));
        logger.info("<==test2");
    }
}
