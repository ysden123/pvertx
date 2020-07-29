/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import com.stulsoft.pvertx.basics.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yuriy Stul
 */
public class EBServiceRunner {
    private static final Logger logger = LoggerFactory.getLogger(EBServiceRunner.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.deployVerticle(EBServiceVerticle.class.getName(), dr -> {
            var count = new AtomicInteger(0);
            for (int i = 1; i <= 10; ++i) {
                vertx.eventBus()
                        .request(
                                EBServiceVerticle.EB_ADDRESS, "test " + i,
                                result -> {
                                    logger.info("Result {}", result.result().body());
                                    if (count.incrementAndGet() >= 10) {
                                        vertx.close();
                                    }
                                });
            }
        });
    }
}
