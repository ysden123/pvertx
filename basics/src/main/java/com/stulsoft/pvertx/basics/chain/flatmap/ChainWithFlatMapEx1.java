/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain.flatmap;

import com.stulsoft.pvertx.common.Utils;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Chaining with flatMap
 *
 * @author Yuriy Stul
 */
public class ChainWithFlatMapEx1 {
    private static final Logger logger = LoggerFactory.getLogger(ChainWithFlatMapEx1.class);

    private final Random random = new Random();

    private final Vertx vertx;

    public static void main(String[] args) {
        logger.info("==>main");
        var i = new ChainWithFlatMapEx1();
        i.test1();
        i.test2();
    }

    private ChainWithFlatMapEx1(){
        vertx = Vertx.newInstance(Utils.createVertx());
    }

    private void test1() {
        logger.info("==>test1");
        process1("start value for test1")
                .flatMap(this::process2)
                .flatMap(this::process3)
                .subscribe(result -> logger.info("test1==> result: {}", result));
    }

    private void test2(){
        logger.info("==>test2");
        process1("start value for test2")
                .flatMap(this::process2WithError)
                .flatMap(this::process3)
                .subscribe(
                        result -> logger.info("test2==> result: {}", result),
                        error -> logger.error("test2==> Failed: {}", error.getMessage()));
    }

    private Single<String> process1(String arg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> source.onSuccess(arg + " processed in 1")));
    }

    private Single<String> process2(String arg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> source.onSuccess(arg + " processed in 2")));
    }

    private Single<String> process2WithError(String arg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> source.onError(new RuntimeException("Test exception in process2WithError"))));
    }

    private Single<String> process3(String arg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> source.onSuccess(arg + " processed in 3")));
    }
}
