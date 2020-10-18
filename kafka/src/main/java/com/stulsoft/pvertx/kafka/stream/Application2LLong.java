/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka.stream;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs service, consumer, and producer.
 * <pre>
 *     - long service
 *     - stream consumer (paused, autocommit, 1 record in pool)
 *     - producer
 * </pre>
 *
 * @author Yuriy Stul
 */
public class Application2LLong {
    private static final Logger logger = LoggerFactory.getLogger(Application2LLong.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();


        Utils.deployVerticles(vertx,
                new String[]{
                        ServiceLongTimeProcessingVerticle.class.getName(),
                        ConsumerStreamOneRecordVerticle.class.getName(),
                        ProducerVerticle.class.getName(),
                })
                .subscribe();
    }
}
