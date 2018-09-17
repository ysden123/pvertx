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
        logger.info("<==main");
    }

    private Single<String> create(final String text) {
        return Single.just(text);
    }

    private Single<String> createWithFail(final String text) {
        return Single.error(new RuntimeException("Test error"));
    }

    private void testCreate() {
        logger.info("==>testCreate");
        create("test text")
                .subscribe(
                        logger::info,
                        err -> logger.error(err.getMessage()));
        logger.info("<==testCreate");
    }
}
