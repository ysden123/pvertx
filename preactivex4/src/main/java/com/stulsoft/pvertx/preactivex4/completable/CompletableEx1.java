/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Success and failure for Completable.
 *
 * @author Yuriy Stul
 */
public class CompletableEx1 {
    private static final Logger logger = LoggerFactory.getLogger(CompletableEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var r1 = f1();
        r1.subscribe(() -> logger.info("Completed"));

        var r2 = f2();
        r2.subscribe(() -> logger.info("Completed"),
                t -> logger.error(t.getMessage(), t));
        logger.info("<==main");
    }

    private static Completable f1() {
        logger.info("==>f1");
        var result = CompletableSubject.create();
        logger.info("<==f1");
        return result.complete();
    }


    private static Completable f2() {
        logger.info("==>f2");
        logger.info("<==f2");
        return Completable.error(new RuntimeException("test error"));
    }

}
