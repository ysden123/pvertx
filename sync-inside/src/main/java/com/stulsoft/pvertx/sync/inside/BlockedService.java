/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.sync.inside;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Yuriy Stul
 */
public class BlockedService {
    private static final Logger logger = LoggerFactory.getLogger(BlockedService.class);

    private final Random random = new Random();
    private final Vertx vertx;

    public BlockedService(Vertx vertx) {
        this.vertx = vertx;
    }

    public void run() {
        logger.info("==>run");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                logger.info("Execute an action");
                try {
                    Thread.sleep(random.nextInt(10_000) + 1);
                } catch (Exception ignore) {
                }
                vertx.eventBus().send(SomeService.EB_ADDRESS, "Working on " + new Date());
            }
        };
        new Timer().schedule(timerTask, 100, 5_000);
    }
}
