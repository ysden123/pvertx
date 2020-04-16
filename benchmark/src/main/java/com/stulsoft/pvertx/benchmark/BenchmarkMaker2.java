/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.benchmark;

import io.reactivex.Completable;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yuriy Stul
 */
public abstract class BenchmarkMaker2 {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkMaker2.class);

    private final LinkedList<Long> durations = new LinkedList<>();
    private final AtomicInteger testCounter;

    protected final Vertx vertx;
    private final int numberOfTests;

    public BenchmarkMaker2(Vertx vertx, final int numberOfTests) {
        Objects.requireNonNull(vertx, "vertx could not be null");
        this.vertx = vertx;
        this.numberOfTests = numberOfTests;
        testCounter = new AtomicInteger(0);
    }

    public Future<Void> run(){
        var promise = Promise.<Void>promise();
        for (var i = 1; i <= numberOfTests; ++i) {
            var start = System.currentTimeMillis();
            f().subscribe(() -> {
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

    protected abstract Completable f();
}
