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
 *     - usual service
 *     - stream consumer (paused, commit) with exception
 *     - producer
 * </pre>
 *
 * @author Yuriy Stul
 */
public class Application7 {
    private static final Logger logger = LoggerFactory.getLogger(Application7.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();


        Utils.deployVerticles(vertx,
                new String[]{
                        ServiceVerticle.class.getName(),
                        ConsumerStreamWithCommitAndExceptionVerticle.class.getName(),
                        ProducerVerticle.class.getName(),
                })
                .subscribe();
    }
}
