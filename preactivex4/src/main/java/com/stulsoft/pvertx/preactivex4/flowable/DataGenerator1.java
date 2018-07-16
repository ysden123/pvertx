/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.flowable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.subjects.CompletableSubject;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Generates Flowable items with specified interval
 *
 * @author Yuriy Stul
 */
public class DataGenerator1 extends Flowable<String> {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator1.class);
    private final int interval;
    private int count;

    /**
     * Initializes a new instance of the DataGenerator1 class
     *
     * @param interval interval between items in milliseconds
     * @param count    number of items
     */
    DataGenerator1(final int interval, final int count) {
        this.interval = interval;
        this.count = count;
    }

    @Override
    protected void subscribeActual(Subscriber<? super String> subscriber) {
        logger.info("==>subscribeActual");
        for (var i = 1; i <= count; ++i) {
            final int iCount = i;
            Completable.timer(interval * i, TimeUnit.MILLISECONDS)
                    .subscribe(() -> {
                        subscriber.onNext(String.format("item %d", iCount));
                        if (iCount == count)
                            subscriber.onComplete();
                    });
        }
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var execution = CompletableSubject.create();
        var f = new DataGenerator1(345, 5);
        var d = f.subscribe(
                logger::info,
                t -> {
                    logger.error(t.getMessage());
                    execution.onComplete();
                },
                () -> {
                    logger.info("Completed");
                    execution.onComplete();
                });

        execution.blockingAwait();
        d.dispose();
        logger.info("<==main");
    }
}
