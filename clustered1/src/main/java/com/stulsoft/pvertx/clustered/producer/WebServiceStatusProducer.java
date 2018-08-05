/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.producer;

import static com.stulsoft.pvertx.clustered.common.Config.*;

import java.util.concurrent.ThreadLocalRandom;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class WebServiceStatusProducer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(WebServiceStatusProducer.class);

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("RandomIdsProducer verticle started");

        vertx.setPeriodic(1000, x -> {

            ClusterManager clusterManager = ((VertxInternal) vertx).getClusterManager();
            logger.info("Nodes: {}", String.join(", ", clusterManager.getNodes()));

            int randomId = ThreadLocalRandom.current().nextInt(0, 3);
            logger.info("Sending data to 'ids' -> " + randomId);
            vertx.eventBus().send(IDS_ADDRESS, randomId);
        });
        startFuture.complete();
    }

    public static void main(String[] args) {
        InfinispanClusterManager cm = new InfinispanClusterManager();
        Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
            if (ar.failed()) {
                System.err.println("Cannot create vert.x instance : " + ar.cause());
            } else {
                Vertx vertx = ar.result();
                vertx.deployVerticle(WebServiceStatusProducer.class.getName());
            }
        });
    }
}
