/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.timer;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class TimerEx1 {
    private static Logger logger = LoggerFactory.getLogger(TimerEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");

        Observable.timer(3, TimeUnit.SECONDS)
                .doOnNext(l -> logger.info("onNext: {}", l))
                .doOnComplete(() -> logger.info("Completed"))
                .subscribe();

        try {
            Thread.sleep(4000);
        } catch (Exception ignore) {
        }
        logger.info("<==main");
    }
}
