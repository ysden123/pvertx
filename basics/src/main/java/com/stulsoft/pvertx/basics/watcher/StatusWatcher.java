/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.watcher;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.internal.util.BlockingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class StatusWatcher {
    private static final Logger logger = LoggerFactory.getLogger(StatusWatcher.class);

    private static final String STATUS_INITIAL = "initial";
    private static final String STATUS_UPDATED = "updated";

    private String status = "initial";

    public static void main(String[] args) {
        logger.info("==>main");

        var sw = new StatusWatcher();
        sw.test1();
        sw.test2();
        logger.info("<==main");
    }

    private Single<String> statusGenerator() {
        return Single.timer(2, TimeUnit.SECONDS)
                .map(l -> {
                    logger.info("In map..");
                    return "Text " + l;
                });
    }

    /**
     * Use of the BlockingHelper.awaitForComplete
     */
    private void test1() {
        logger.info("==>test1");
        var latch = new CountDownLatch(1);
        var statusWatcher = new StatusWatcher();
        var id = statusWatcher.statusGenerator()
                .subscribe(
                        s -> {
                            logger.info("Success {}", s);
                            latch.countDown();
                        },
                        error -> {
                            logger.error(error.getMessage());
                            latch.countDown();
                        }
                );

        BlockingHelper.awaitForComplete(latch, id);
        logger.info("<==test1");
    }

    /**
     * Use of the distinctUntilChanged
     */
    private void test2() {
        logger.info("==>test2");
        changeStatus(STATUS_INITIAL);

        // Change status after 3 seconds
        Completable.timer(3, TimeUnit.SECONDS)
                .subscribe(() -> changeStatus(STATUS_UPDATED));

        // Check status every 1 second
        var latch = new CountDownLatch(1);
        var id = Observable.interval(1, TimeUnit.SECONDS)
                // Skip STATUS_INITIAL - to prevent 1st time test
                .filter(l -> !getStatus().equals(STATUS_INITIAL))
                .map(l -> getStatus())
                .distinctUntilChanged()
                .subscribe(
                        status -> {
                            logger.info("Success {}", status);
                            latch.countDown();
                        },
                        error -> {
                            logger.error(error.getMessage());
                            latch.countDown();
                        }
                );

        // Wait 1st change
        BlockingHelper.awaitForComplete(latch, id);
        logger.info("<==test2");
    }

    private String getStatus() {
        logger.info("Getting status {}", status);
        return status;
    }

    private void changeStatus(String newStatus) {
        logger.info("Changing status to {}", status);
        status = newStatus;
    }
}
