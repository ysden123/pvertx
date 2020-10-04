/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.msgqueue;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs not sequential service; uses eventBus().send
 *
 * @author Yuriy Stul
 */
public class NotSeqServiceRunner2 {
    private static final Logger logger = LoggerFactory.getLogger(NotSeqServiceRunner2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.deployVerticle(NotSeqServiceVerticle.class.getName(),
                dr -> {
                    for (int i = 1; i <= 10; ++i) {
                        var message = "msg # " + i;
                        logger.info("Sending {}", message);

                        vertx.eventBus().send(NotSeqServiceVerticle.EB_ADDRESS,
                                message);
                    }
                });
    }
}
