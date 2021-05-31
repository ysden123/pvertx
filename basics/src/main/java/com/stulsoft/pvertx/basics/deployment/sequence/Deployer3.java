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
public class Deployer3 {
    private static final Logger logger = LoggerFactory.getLogger(Deployer3.class);

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
    }

    private static void test1(){
        logger.info("==>test1");
        var vertx= Utils.createVertx();

        Utils.deployVerticles(vertx,
                new String[]{Verticle21.class.getName(),
                        Verticle22.class.getName(),
                        Verticle23.class.getName()})
        .subscribe(vertx::close);
    }
}
