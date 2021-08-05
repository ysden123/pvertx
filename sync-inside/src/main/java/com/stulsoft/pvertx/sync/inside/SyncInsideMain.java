/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.sync.inside;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SyncInsideMain {
    private static final Logger logger = LoggerFactory.getLogger(SyncInsideMain.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        var blockedService = new BlockedService(vertx);

        vertx.deployVerticle(new SomeService()).onSuccess(__ -> {
            blockedService.run();
        });
    }
}
