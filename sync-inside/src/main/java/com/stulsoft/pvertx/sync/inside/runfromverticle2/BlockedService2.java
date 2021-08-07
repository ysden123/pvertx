/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.sync.inside.runfromverticle2;

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
public class BlockedService2 {
    private static final Logger logger = LoggerFactory.getLogger(BlockedService2.class);

    private final Random random = new Random();
    
    public void run(Vertx vertx) {
        logger.info("==>run");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                logger.info("Execute an action");
                try {
                    Thread.sleep(random.nextInt(10_000) + 1);
                } catch (Exception ignore) {
                }
                vertx.eventBus().send(ServiceWithBlockedVerticle.EB_ADDRESS, "Working on " + new Date());
            }
        };
        new Timer().schedule(timerTask, 100, 5_000);
    }
}
