/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.sync.inside.runfromverticle;

import com.stulsoft.pvertx.sync.inside.servise.BlockedService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class StartVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(StartVerticle.class);

    private final BlockedService blockedService;

    public StartVerticle(BlockedService blockedService) {
        this.blockedService = blockedService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("==>start");
        blockedService.run(vertx);
        super.start(startPromise);
    }
}
