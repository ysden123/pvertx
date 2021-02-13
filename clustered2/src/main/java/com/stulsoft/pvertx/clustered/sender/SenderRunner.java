/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs Sender verticle
 *
 * @author Yuriy Stul
 */
public class SenderRunner {
    private static final Logger logger = LoggerFactory.getLogger(SenderRunner.class);

    public static void main(String[] args) {
        final var mgr = new HazelcastClusterManager(ClusterConfiguratorHelper.getHazelcastConfiguration());
        final var options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, cluster -> {
            if (cluster.succeeded()) {
                logger.info("Cluster started");
                // deploy Sender
                cluster.result().deployVerticle(Sender.class.getName(), deployResult -> {
                    if (deployResult.succeeded()) {
                        logger.info("Sender deployed");
                        final var eb = cluster.result().eventBus();
                        var counter = new AtomicInteger(0);
                        cluster.result().setPeriodic(5000,
                                l -> eb.request(
                                Sender.EB_ADDRESS,
                                "Sending message for " + counter.incrementAndGet(),
                                sendResult -> {
                                    if (sendResult.succeeded()) {
                                        logger.info("Response from service: {}", sendResult.result().body());
                                    } else {
                                        logger.warn("Response from service has not received. {}", sendResult.cause().getMessage());
                                    }
                                }));
                    } else {
                        logger.error("Failed deploy Sender. {}", deployResult.cause().getMessage());
                    }
                });
            } else {
                logger.error("Failed start cluster. {}", cluster.cause().getMessage());
            }
        });
    }

}
