/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.msgqueue;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs sequential service; uses eventBus().request
 *
 * @author Yuriy Stul
 */
public class SeqServiceRunner {
    private static final Logger logger = LoggerFactory.getLogger(SeqServiceRunner.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.deployVerticle(SeqServiceVerticle.class.getName(),
                dr -> {
                    for (int i = 1; i <= 10; ++i) {
                        var message = "msg # " + i;
                        logger.info("Sending {}", message);

                        vertx.eventBus().request(SeqServiceVerticle.EB_ADDRESS,
                                message,
                                result -> logger.info("Result for {} is {}", message, result.result().body()));
                    }
                });
    }
}
