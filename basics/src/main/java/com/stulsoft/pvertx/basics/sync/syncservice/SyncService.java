/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.sync.syncservice;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SyncService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);
    private final Vertx vertx;

    public SyncService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void run() {
        logger.info("==>run");
        while(true){
            logger.info("Processing an input ...");
            vertx.eventBus().send(VerticleService.EB_ADDRESS, "msg " + System.currentTimeMillis());
            try{
                Thread.sleep(10000);
            }catch (Exception ignore){}
        }
    }
}
