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
public class TimerEx1 {
    private static final Logger logger = LoggerFactory.getLogger(TimerEx1.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("==>main");
        f1();

        Thread.sleep(2000);
        logger.info("<==main");
    }

    private static void f1() {
        logger.info("==>f1");

        Completable.timer(1, TimeUnit.SECONDS)
                .doOnComplete(() -> {
                    logger.info("On complete");
                })
                .subscribe();
        logger.info("<==f1");
    }
}
