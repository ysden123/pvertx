/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.single;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SingleEx2 {
    private static final Logger logger = LoggerFactory.getLogger(SingleEx2.class);

    static Single<String> generateString1() {
        logger.info("==>generateString1");
        return Single.create(emitter -> emitter.onSuccess("Test 1"));
    }

    static Single<String> generateString2() {
        logger.info("==>generateString2");
        return Single.create(emitter -> {
            try {
                Thread.sleep(1000);
                emitter.onSuccess("Test 2");
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    static Single<String> generateString3() {
        return Single.create(emitter -> {
            new Thread(() -> {
                logger.info("==>generateString3");
                try {
                    Thread.sleep(1000);
                    emitter.onSuccess("Test 3");
                } catch (Exception ex) {
                    emitter.onError(ex);
                }

            }).start();
        });
    }

    static void test1() {
        logger.info("==>test1");
        generateString1().subscribe(
                text -> logger.info("Text: {}", text),
                error -> logger.error(error.getMessage()));
        logger.info("<==test1");
    }

    static void test2() {
        logger.info("==>test2");
        generateString2().subscribe(
                text -> logger.info("Text: {}", text),
                error -> logger.error(error.getMessage()));
        logger.info("<==test2");
    }

    static void test3() {
        logger.info("==>test3");
        generateString3().subscribe(
                text -> logger.info("Text: {}", text),
                error -> logger.error(error.getMessage()));
        logger.info("<==test3");
    }

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        test2();
        test3();
        logger.info("<==main");
    }
}
