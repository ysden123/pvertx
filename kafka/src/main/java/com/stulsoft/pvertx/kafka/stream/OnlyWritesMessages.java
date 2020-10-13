/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs producer.
 * <pre>
 *     - producer
 * </pre>
 *
 * @author Yuriy Stul
 */
public class OnlyWritesMessages {
    private static final Logger logger = LoggerFactory.getLogger(OnlyWritesMessages.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();


        Utils.deployVerticles(vertx,
                new String[]{
                        ProducerVerticle.class.getName(),
                })
                .subscribe();
    }
}
