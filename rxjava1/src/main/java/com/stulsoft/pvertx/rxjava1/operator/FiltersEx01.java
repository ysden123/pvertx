/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.operator;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class FiltersEx01 {
    private static Logger logger = LoggerFactory.getLogger(FiltersEx01.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Observable<String> lines = Observable.just("text", "#text", "222text");
        Observable<String> comments = lines.filter(s -> s.startsWith("#"));
        Observable<String> digits = lines.filter(s -> s.startsWith("222"));

        logger.info("All:");
        lines.doOnNext(logger::info).subscribe();

        logger.info("");
        logger.info("Comments:");
        comments.doOnNext(logger::info).subscribe();

        logger.info("");
        logger.info("Digits:");
        digits.doOnNext(logger::info).subscribe();

        logger.info("<==main");
    }
}
