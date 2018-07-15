/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class Success2 extends Completable {
    private static final Logger logger = LoggerFactory.getLogger(Success2.class);

    @Override
    protected void subscribeActual(CompletableObserver completableObserver) {
        logger.info("==>subscribeActual");
        timer(1, TimeUnit.SECONDS)
                .andThen(Completable.complete())
                .subscribe(completableObserver::onComplete);
    }
}
