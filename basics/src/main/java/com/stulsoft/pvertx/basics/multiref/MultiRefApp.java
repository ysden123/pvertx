/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiref;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.stulsoft.pvertx.basics.multiref.SomeService.EB_ADDRESS;

/**
 * @author Yuriy Stul
 */
public class MultiRefApp {
    private static final Logger logger = LoggerFactory.getLogger(MultiRefApp.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();

        vertx.deployVerticle(SomeService.class.getName(), dr -> {
            for (int i = 1; i <= 10; ++i) {
                String msg = "msg " + i;
                vertx.eventBus().request(EB_ADDRESS, msg, ar -> {
                  logger.info("After reply on {}", msg);
                });
            }
        });
    }

}
