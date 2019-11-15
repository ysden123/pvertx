/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.httpserver.cluster;

import com.stulsoft.pvertx.common.Terminator;
import com.stulsoft.pvertx.httpserver.cluster.verticle.Server;
import com.stulsoft.pvertx.httpserver.cluster.verticle.Service;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Started Main");

        runWithoutCluster();

//        runWithCluster();
    }

    private static void runWithCluster() {
        logger.info("==>runWithCluster");
        final var mgr = new HazelcastClusterManager(ClusterConfiguratorHelper.getHazelcastConfiguration());
//        final var mgr = new HazelcastClusterManager();
        final var options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, cluster -> {
            if (cluster.succeeded()) {
                logger.info("Cluster started");

                // Gracefully stop Vertx
                new Thread(() -> {
                    Terminator.terminate(cluster.result());
                    logger.info("Stopped Main");
                }).start();

                deployServer(cluster.result());
                deployService(cluster.result());
            } else {
                logger.error("Cluster up failed: " + cluster.cause());
                System.exit(3);
            }
        });
    }

    private static void runWithoutCluster() {
        logger.info("==>runWithoutCluster");
        var vertx = Vertx.vertx();

        deployService(vertx);

        deployServer(vertx);

        // Gracefully stop Vertx
        new Thread(() -> {
            Terminator.terminate(vertx);
            logger.info("Stopped Main");
        }).start();
    }

    // Deploys one Server instance
    private static void deployServer(final Vertx vertx) {
        var server = new Server();

        logger.info("Deploying HttpServer");
        vertx.deployVerticle(server, ar -> {
            if (ar.succeeded())
                logger.info("Server started");
            else {
                logger.error("Server failed " + ar.cause().getMessage());
                vertx.close();
                System.exit(1);
            }
        });
    }

    // Deploys many Service instances
    private static void deployService(final Vertx vertx) {
        vertx.deployVerticle(Service.class.getName(),
                new DeploymentOptions().setInstances(10),
                ar -> {
                    if (ar.succeeded())
                        logger.info("Service instances started");
                    else {
                        logger.error("Service failed " + ar.cause().getMessage());
                        vertx.close();
                        System.exit(2);
                    }
                });
    }
}
