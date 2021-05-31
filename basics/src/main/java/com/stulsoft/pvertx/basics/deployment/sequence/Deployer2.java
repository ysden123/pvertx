/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.deployment.sequence;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.DeploymentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Deployer2 {
    private static final Logger logger = LoggerFactory.getLogger(Deployer2.class);

    public static void main(String[] args) {
        logger.info("==>main");
//        test1();
//        test2();
//        test3();
        test4();
    }

    private static void test1(){
        logger.info("==>test1");
        var vertx= Utils.createVertx();

        vertx.deployVerticle(new Verticle21());
        vertx.deployVerticle(new Verticle22());
        vertx.deployVerticle(new Verticle23());
    }

    private static void test2(){
        logger.info("==>test2");
        var vertx= Utils.createVertx();
        vertx.deployVerticle(new Verticle21())
                .compose(__ -> vertx.deployVerticle(new Verticle22()))
                .compose(__ -> vertx.deployVerticle(new Verticle23()))
        .onComplete(__ -> logger.debug("Deployment is complete"));
    }

    private static void test3(){
        logger.info("==>test3");
        var vertx= Utils.createVertx();
        vertx.deployVerticle(new Verticle21())
                .flatMap(__ -> vertx.deployVerticle(new Verticle22()))
                .flatMap(__ -> vertx.deployVerticle(new Verticle23()))
        .onComplete(__ -> logger.debug("Deployment is complete"));
    }

    private static void test4(){
        logger.info("==>test4");
        var vertx= Utils.createVertx();
        var deploymentOptions = new DeploymentOptions()
                .setInstances(2);
        vertx.deployVerticle(Verticle21.class.getName(), deploymentOptions)
                .compose(__ -> vertx.deployVerticle(Verticle22.class.getName(), deploymentOptions))
                .compose(__ -> vertx.deployVerticle(Verticle23.class.getName(), deploymentOptions))
                .onComplete(__ -> logger.debug("Deployment is complete"));
    }
}
