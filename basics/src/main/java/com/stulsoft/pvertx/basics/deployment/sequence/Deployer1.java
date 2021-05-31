/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.deployment.sequence;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Deployer1 {
    private static final Logger logger = LoggerFactory.getLogger(Deployer1.class);

    public static void main(String[] args) {
        logger.info("==>main");
//        test1();
//        test2();
        test3();
    }

    private static void test1(){
        logger.info("==>test1");
        var vertx= Utils.createVertx();

        vertx.deployVerticle(new Verticle1());
        vertx.deployVerticle(new Verticle2());
        vertx.deployVerticle(new Verticle3());
    }

    private static void test2(){
        logger.info("==>test2");
        var vertx= Utils.createVertx();
        vertx.deployVerticle(new Verticle1())
                .compose(__ -> vertx.deployVerticle(new Verticle2()))
                .compose(__ -> vertx.deployVerticle(new Verticle3()))
        .onComplete(__ -> logger.debug("Deployment is complete"));
    }

    private static void test3(){
        logger.info("==>test3");
        var vertx= Utils.createVertx();
        vertx.deployVerticle(new Verticle1())
                .flatMap(__ -> vertx.deployVerticle(new Verticle2()))
                .flatMap(__ -> vertx.deployVerticle(new Verticle3()))
        .onComplete(__ -> logger.debug("Deployment is complete"));
    }
}
