/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.sync.syncservice;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

/**
 * @author Yuriy Stul
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var executor = Executors.newSingleThreadExecutor();

        var vertx = Vertx.vertx();

        vertx.deployVerticle(VerticleService.class.getName(), dr ->{
            logger.info("Starting sync service ...");

            executor.execute(new SyncService(vertx));
        });
    }
}
