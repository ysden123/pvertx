/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.sync.syncservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class VerticleService extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(VerticleService.class);
    public static final String EB_ADDRESS = VerticleService.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<String> message){
        logger.info("Handling {}", message.body());
        message.reply("Done for " + message.body());
    }

}
