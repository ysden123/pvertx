/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.watcher;

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

    private String status = "initial";

    public static void main(String[] args) {
        logger.info("==>main");
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
        logger.info("<==main");
    }

    private Single<String> statusGenerator() {
        return Single.timer(2, TimeUnit.SECONDS)
                .map(l -> {
                    logger.info("In map..");
                    return "Text " + l;
                });
    }

    private void test1(){
        logger.info("==>test1");

        logger.info("<==test1");
    }

    private String getStatus() {
        return status;
    }

    private void changeStatus(String newStatus) {
        status = newStatus;
    }
}
