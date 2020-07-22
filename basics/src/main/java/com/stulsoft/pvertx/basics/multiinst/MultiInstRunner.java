/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiinst;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class MultiInstRunner {
    private static final Logger logger = LoggerFactory.getLogger(ServiceForMultiInst.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        var deploymentOptions = new DeploymentOptions()
                .setInstances(2);

        vertx.deployVerticle(ServiceForMultiInst.class.getName(), deploymentOptions, deployAr ->{
            if (deployAr.succeeded()){
                vertx.eventBus().send(ServiceForMultiInst.EB_ADDRESS, "do it send 1");
                vertx.eventBus().send(ServiceForMultiInst.EB_ADDRESS, "do it send 2");

                vertx.eventBus().publish(ServiceForMultiInst.EB_ADDRESS, "do it publish 1");

                vertx.setTimer(3000, l -> vertx.close());
            }else{
                logger.error("Deployment failed. {}", deployAr.cause().getMessage());
                vertx.close();
            }
        });
    }
}
