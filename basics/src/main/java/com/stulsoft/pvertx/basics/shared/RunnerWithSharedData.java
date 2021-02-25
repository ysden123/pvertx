/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.shared;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.DeploymentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class RunnerWithSharedData {
    private static final Logger logger = LoggerFactory.getLogger(RunnerWithSharedData.class);

    public static final String COUNTER_NAME = "SHARED_COUNTER";
    public static final String MAP_NAME = "SHARED_MAP";
    public static final String MAP_NAME2 = "SHARED_MAP2";

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.sharedData().getCounter(COUNTER_NAME, counter -> {
            counter.result().get(counterValue -> {
                logger.info("Counter value = {}", counterValue.result());
            });
        });

        try {
            vertx.sharedData().getClusterWideMap(MAP_NAME, map -> {
                if (map.succeeded()) {
                    logger.info("getClusterWideMap: Has get map");
                } else {
                    logger.error("(1) getClusterWideMap: Failed get map: " + map.cause().getMessage());
                }
            });
        }catch(Exception ex){
            logger.error("(2) getClusterWideMap: Failed get map: " + ex.getMessage());
        }

        try {
            vertx.sharedData().getAsyncMap(MAP_NAME2, map -> {
                if (map.succeeded()) {
                    logger.info("getAsyncMap: Has get map");
                } else {
                    logger.error("(1) getAsyncMap: Failed get map: " + map.cause().getMessage());
                }
            });
        }catch(Exception ex){
            logger.error("(2) getAsyncMap: Failed get map: " + ex.getMessage());
        }

        var deploymentOptions = new DeploymentOptions()
                .setInstances(4);
        vertx.deployVerticle(Service2Verticle.class.getName(), deploymentOptions,
                dr -> vertx.setPeriodic(1500L,
                        l -> vertx.eventBus().<String>request(Service2Verticle.EB_ADDRESS, "test",
                                ar -> logger.info("Response: {}", ar.result().body()))));
    }
}
