/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @see Single2
 */
public class Single4 {
    private static final Logger logger = LoggerFactory.getLogger(Single4.class);

    public static void main(String[] args) {
        logger.info("==>main");
        String[] result = {""};
        Observable<String> observer = Observable.just("Hello"); // provides data
        observer
                .doOnComplete(() -> logger.info("(1) result[0]:{}", result[0]))
                .doOnDispose(() -> logger.info("==>onDispose"))
                .subscribe(s -> result[0] = s) // Callable as subscriber
                .dispose();

        logger.info("(2) result[0]:{}", result[0]);
        logger.info("<==main");
    }
}
