/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class V2 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(V2.class);
    @Override
    public void start() throws Exception {
        logger.info("==>start");
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("==>stop");
        super.stop();
    }
}
