/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.sync.inside.runfromverticle2;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Run2 {
    private static final Logger logger = LoggerFactory.getLogger(Run2.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Vertx.vertx();

        vertx.deployVerticle(new ServiceWithBlockedVerticle());
    }
}
