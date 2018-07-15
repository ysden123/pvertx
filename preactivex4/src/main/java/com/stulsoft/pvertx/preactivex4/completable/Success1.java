/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.subjects.CompletableSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class Success1 extends Completable {
    private static final Logger logger = LoggerFactory.getLogger(Success1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var execution = CompletableSubject.create();

        var s = new Success1();
        var d = s.subscribe(() -> {
                    logger.info("(2) Completed");
                    execution.onComplete();
                },
                t -> logger.error(t.getMessage(), t));

        execution.blockingAwait();
        d.dispose();
        logger.info("<==main");
    }

    @Override
    protected void subscribeActual(CompletableObserver completableObserver) {
        logger.info("==>subscribeActual");
        timer(1, TimeUnit.SECONDS)
                .andThen(Completable.complete())
                .subscribe(completableObserver::onComplete);
    }
}
