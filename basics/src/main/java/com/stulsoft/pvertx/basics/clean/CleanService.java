/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.clean;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class CleanService extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(CleanService.class);

    public static final String EB_ADDRESS = CleanService.class.getName();

    @Override
    public void start() throws Exception {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start();
    }

    private void handler(Message<CleanMessage> message){
        logger.info("message: {}", message.body().body().encodePrettily());
        message.reply("Done");
    }
}
