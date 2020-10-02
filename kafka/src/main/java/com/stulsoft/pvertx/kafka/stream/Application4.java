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
 *     - service with exception in sub-thread (setTimer)
 *     - stream consumer (paused)
 *     - producer
 * </pre>
 *
 * @author Yuriy Stul
 */
public class Application4 {
    private static final Logger logger = LoggerFactory.getLogger(Application4.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();


        Utils.deployVerticles(vertx,
                new String[]{
                        ServiceWithExceptionVerticle.class.getName(),
                        ConsumerStreamVerticle.class.getName(),
                        ProducerVerticle.class.getName(),
                })
                .subscribe();
    }
}
