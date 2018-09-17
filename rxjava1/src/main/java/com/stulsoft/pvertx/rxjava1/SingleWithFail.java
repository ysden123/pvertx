/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SingleWithFail {
    private static Logger logger = LoggerFactory.getLogger(SingleWithFail.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var o = new SingleWithFail();
        o.testCreate();
        o.testCreateWithFail();
        o.testCreateWitSideEffect();
        logger.info("<==main");
    }

    private Single<String> create(final String text) {
        return Single.just(text);
    }

    private Single<String> createWithFail(final String text) {
        return Single.error(new RuntimeException(text));
    }

    private String concat(final String original, final String adition) {
        return original + "_" + adition;
    }

    private void testCreate() {
        logger.info("==>testCreate");
        create("test text")
                .map(s -> concat(s, "some additional text"))
                .subscribe(
                        s -> logger.info(s),
                        err -> logger.error(err.getMessage()))
                .dispose();
        logger.info("<==testCreate");
    }

    private void testCreateWithFail() {
        logger.info("==>testCreateWithFail");
        createWithFail("text for fail test")
                .map(s -> concat(s, "some additional text for fail test"))
                .subscribe(
                        s -> logger.info(s),
                        err -> logger.error(err.getMessage()))
                .dispose();
        logger.info("<==testCreateWithFail");
    }

    private void testCreateWitSideEffect() {
        logger.info("==>testCreateWitSideEffect");
        String[] result = {""};
        create("test text for side effect")
                .map(s -> concat(s, "some additional text"))
                .subscribe(
                        s -> {
                            logger.info(s);
                            result[0] = s;
                        },
                        err -> logger.error(err.getMessage()))
                .dispose();
        logger.info("result=\"{}\"", result[0]);
        logger.info("<==testCreateWitSideEffect");
    }
}
