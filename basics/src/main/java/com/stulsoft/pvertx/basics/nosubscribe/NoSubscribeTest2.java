/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.nosubscribe;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class NoSubscribeTest2 {
    private static final Logger logger = LoggerFactory.getLogger(NoSubscribeTest2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var noSubscribeTest1 = new NoSubscribeTest2();
        noSubscribeTest1.testWithoutSubscribe();
        noSubscribeTest1.testWitSubscribe();

        try {
            Thread.sleep(2_000);
        } catch (Exception ignore) {
        }
        logger.info("<==main");
    }

    private Single<String> func1() {
        logger.info("==>func1");
        logger.info("func1 inside");
        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }
        return Single.create(source -> {
            source.onSuccess("func1 is completed");
            logger.info("<== from inside of func1");
        });
    }

    private void testWithoutSubscribe() {
        logger.info("==>testWithoutSubscribe");
        func1();
        logger.info("<==testWithoutSubscribe");
    }

    private void testWitSubscribe() {
        logger.info("==>testWitSubscribe");
        func1().subscribe(result -> logger.info("Result: {}", result));
        logger.info("<==testWitSubscribe");
    }
}
