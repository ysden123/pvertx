/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * receiver runner
 *
 * @author Yuriy Stul
 */
public class ServiceRunner {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRunner.class);

    public static void run(final String serviceInstanceName) {
        final var mgr = new HazelcastClusterManager(ClusterConfiguratorHelper.getHazelcastConfiguration());
        final var options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, cluster -> {
            if (cluster.succeeded()) {
                logger.info("Cluster started");
                cluster.result().deployVerticle(new Service(serviceInstanceName), res -> {
                    if (res.succeeded()) {
                        logger.info("Deployment id is: {} ", res.result());
                    } else {
                        logger.error("Deployment failed!");
                    }
                });
            } else {
                logger.error("Cluster up failed: " + cluster.cause());
            }
        });
    }
}
