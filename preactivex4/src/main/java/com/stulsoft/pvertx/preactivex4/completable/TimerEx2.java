/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class TimerEx2 {
    private static final Logger logger = LoggerFactory.getLogger(TimerEx2.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("==>main");
        var c = f1();
        c.subscribe(() -> logger.info("Completed"));
        c.blockingAwait();
        logger.info("<==main");
    }

    private static Completable f1() {
        logger.info("==>f1");
        return Completable.timer(1, TimeUnit.SECONDS);
    }
}
