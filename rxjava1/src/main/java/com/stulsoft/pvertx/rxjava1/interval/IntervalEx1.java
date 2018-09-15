/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.interval;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Interval generates endless events with specified time period
 *
 * @author Yuriy Stul
 */
public class IntervalEx1 {
    private static Logger logger = LoggerFactory.getLogger(IntervalEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");

        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(l -> logger.info("onNext: {}", l))
                .doOnComplete(() -> logger.info("Completed"))
                .subscribe();

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (Exception ignore) {
        }
        logger.info("<==main");
    }
}
