/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.operator;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Few filters for same stream.
 *
 * @author Yuriy Stul
 */
public class FiltersEx01 {
    private static Logger logger = LoggerFactory.getLogger(FiltersEx01.class);

    private static void withDoOnNext() {
        logger.info("==>withDoOnNext");
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
        logger.info("<==withDoOnNext");
    }

    private static void withoutDoOnNext() {
        logger.info("==>withoutDoOnNext");
        Observable<String> lines = Observable.just("text", "#text", "222text");
        Observable<String> comments = lines.filter(s -> s.startsWith("#"));
        Observable<String> digits = lines.filter(s -> s.startsWith("222"));

        logger.info("All:");
        lines.subscribe(logger::info).dispose();

        logger.info("");
        logger.info("Comments:");
        comments.subscribe(logger::info).dispose();

        logger.info("");
        logger.info("Digits:");
        digits.subscribe(logger::info).dispose();
        logger.info("<==withoutDoOnNext");
    }

    public static void main(String[] args) {
        logger.info("==>main");

        withDoOnNext();
        withoutDoOnNext();

        logger.info("<==main");
    }
}
