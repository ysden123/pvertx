/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.flowable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.CompletableSubject;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * enerates Flowable items with specified interval using Scheduler
 *
 * @author Yuriy Stul
 */
public class DataGenerator2 extends Flowable<String> {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator2.class);

    private final int interval;
    private int count;

    DataGenerator2(final int interval, final int count) {
        this.interval = interval;
        this.count = count;
    }

    @Override
    protected void subscribeActual(Subscriber<? super String> subscriber) {
        logger.info("==>subscribeActual");
        var s = Observable.interval(interval, interval, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
                .subscribe(l -> {
                    subscriber.onNext(String.format("item %d", l));
                    if (l >= count - 1) {
                        subscriber.onComplete();
                    }
                });
    }


    public static void main(String[] args) {
        logger.info("==>main");
        var execution = CompletableSubject.create();
        var f = new DataGenerator2(345, 5);
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
