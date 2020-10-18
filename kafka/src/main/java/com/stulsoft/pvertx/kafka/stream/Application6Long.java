/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs long service, consumer, and producer.
 * <pre>
 *     - long time processing service
 *     - stream consumer (paused, commit)
 *     - producer
 * </pre>
 *
 * @author Yuriy Stul
 */
public class Application6Long {
    private static final Logger logger = LoggerFactory.getLogger(Application6Long.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();


        Utils.deployVerticles(vertx,
                new String[]{
                        ServiceLongTimeProcessingVerticle.class.getName(),
                        ConsumerStreamWithCommitVerticle.class.getName(),
                        ProducerVerticle.class.getName(),
                })
                .subscribe();
    }
}
