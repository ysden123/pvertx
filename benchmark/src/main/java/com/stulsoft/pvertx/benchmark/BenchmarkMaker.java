/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.benchmark;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author Yuriy Stul
 */
public class BenchmarkMaker {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkMaker.class);

    private final LinkedList<Long> durations = new LinkedList<>();
    private final AtomicInteger testCounter;

    private final Vertx vertx;
    private final int numberOfTests;

    public BenchmarkMaker(Vertx vertx, final int numberOfTests) {
        Objects.requireNonNull(vertx, "vertx could not be null");
        this.vertx = vertx;
        this.numberOfTests = numberOfTests;
        testCounter = new AtomicInteger(0);
    }

    public Future<Void> run(Function<Vertx, Completable> f) {
        var promise = Promise.<Void>promise();
        for (var i = 1; i <= numberOfTests; ++i) {
            var start = System.currentTimeMillis();
            f.apply(vertx).subscribe(() -> {
                durations.add(System.currentTimeMillis() - start);
                if (testCounter.incrementAndGet() == numberOfTests) {
                    showResults();
                    promise.complete();
                }
            });
        }
        return promise.future();
    }

    private void showResults() {
        logger.info("==>showResults");
        logger.debug(durations.toString());
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var bm = new BenchmarkMaker(vertx, 3);

        bm.run(v -> {
            return Completable.create(s -> {
                v.setTimer(1500, l -> {
                    logger.debug("==>In function");
                    s.onComplete();
                });
            });
        }).onComplete(ar -> {
            vertx.close();
            logger.info("<==main");
        });
    }
}
