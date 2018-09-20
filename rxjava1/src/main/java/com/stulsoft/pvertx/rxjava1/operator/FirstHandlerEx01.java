/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.operator;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Change 1st item
 *
 * @author Yuriy Stul
 */
public class FirstHandlerEx01 {
    private static Logger logger = LoggerFactory.getLogger(FirstHandlerEx01.class);

    private static void skipFirst() {
        logger.info("==>skipFirst");
        Observable.just("1st", "2nd", "3rd")
                .first("tttt")
                .subscribe(s -> logger.info(s));
        logger.info("<==skipFirst");
    }

    private static void handleFirst() {
        logger.info("==>handleFirst");
        final var counter = new AtomicInteger(0);
        Observable.just("1st", "2nd", "3rd")
                .toFlowable(BackpressureStrategy.BUFFER)
                .map(i -> {
                    if (counter.getAndIncrement() == 0) {
                        // Skip 1st
                        logger.info("Skip 1st");
                        return Observable.empty();
                    } else {
                        return Observable.just(i);
                    }
                })
                .subscribe(s -> logger.info(s.toString()));
        logger.info("<==handleFirst");
    }

    public static void main(String[] args) {
        logger.info("==>main");
        skipFirst();
        handleFirst();
        logger.info("<==main");
    }
}
