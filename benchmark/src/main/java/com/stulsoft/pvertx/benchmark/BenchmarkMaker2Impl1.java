/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.benchmark;

import io.reactivex.Completable;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class BenchmarkMaker2Impl1 extends BenchmarkMaker2 {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkMaker2Impl1.class);

    BenchmarkMaker2Impl1(Vertx vertx, final int numberOfTests) {
        super(vertx, numberOfTests);
    }

    @Override
    protected Completable f() {
        return Completable.create(source -> vertx.setTimer(1500, l -> {
            logger.debug("==> In function");
            source.onComplete();
        }));
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var bm = new BenchmarkMaker2Impl1(vertx, 3);
        bm.run().onComplete(ar -> {
            vertx.close();
            logger.info("<==main");
        });
    }
}
