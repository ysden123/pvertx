/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.sync.inside.runfromverticle;

import com.stulsoft.pvertx.sync.inside.servise.BlockedService;
import com.stulsoft.pvertx.sync.inside.servise.SomeService;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class RunFromVerticleMain {
    private static final Logger logger = LoggerFactory.getLogger(RunFromVerticleMain.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Vertx.vertx();

        var blockedService = new BlockedService();
        vertx.deployVerticle(new SomeService())
                .flatMap(id -> vertx.deployVerticle(new StartVerticle(blockedService)));
    }
}
