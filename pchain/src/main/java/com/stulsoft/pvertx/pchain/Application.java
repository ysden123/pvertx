/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pchain;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();

        vertx.deployVerticle(ServiceVerticle.class.getName(), dr -> ChainMngr1.manager2(20, vertx)
                .subscribe(() -> {
                            vertx.close();
                            logger.info("<==main");
                        },
                        err -> logger.error(err.getMessage())));
    }
}
