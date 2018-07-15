/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.completable;

import io.reactivex.subjects.CompletableSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class AndThenEx1 {
    private static final Logger logger = LoggerFactory.getLogger(AndThenEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        testAllSuccess();
        testOneError();
        logger.info("<==main");
    }

    private static void testAllSuccess() {
        logger.info("==>testAllSuccess");

        var execution = CompletableSubject.create();

        var d = (new Success1())
                .andThen(new Success2())
                .subscribe(() -> {
                            logger.info("Total success");
                            execution.onComplete();
                        },
                        t -> {
                            logger.error("Total error: {}", t.getMessage());
                            execution.onComplete();
                        });

        execution.blockingAwait();
        d.dispose();
        logger.info("<==testAllSuccess");
    }

    private static void testOneError() {
        logger.info("==>testOneError");
        var execution = CompletableSubject.create();

        var d = (new Success1())
                .andThen(new Error1())
                .andThen(new Success2())    // Never works
                .subscribe(() -> {
                            logger.info("Total success");
                            execution.onComplete();
                        },
                        t -> {
                            logger.error("Total error: {}", t.getMessage());
                            execution.onComplete();
                        });

        execution.blockingAwait();
        d.dispose();

        logger.info("<==testOneError");
    }
}
