/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.Completable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class TimeoutEx1 {
    private static final Logger logger = LoggerFactory.getLogger(TimeoutEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var s = Completable.create(emitter -> {
            Completable
                    .complete()
                    .delay(2, TimeUnit.SECONDS)
                    .subscribe(() -> emitter.onComplete());
        });

        s.timeout(1, TimeUnit.SECONDS)
                .subscribe(() -> logger.info("Completed"),
                        t -> {
                            logger.error("On error: {} {} {}",
                                    t.getClass().getSimpleName(),
                                    t.getCause(),
                                    t.getMessage());
                        });

        s.blockingAwait();
        logger.info("<==main");
    }
}
