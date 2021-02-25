/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.shared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.stulsoft.pvertx.basics.shared.RunnerWithSharedData.COUNTER_NAME;

/**
 * @author Yuriy Stul
 */
public class Service2Verticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Service2Verticle.class);

    public static final String EB_ADDRESS = Service2Verticle.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        logger.info("{} has been started", this.getClass().getSimpleName());
    }

    private void handler(Message<String> message) {
        logger.info("Handling {}", message.body());
        vertx.sharedData().getCounter(COUNTER_NAME, counter -> {
            counter.result().get(counterValue -> {
                logger.info("Counter value = {}", counterValue.result());
            });
            counter.result().getAndIncrement(counterChanged -> {
                logger.info("Counter changed value = {}", counterChanged.result());
            });
        });
        message.reply("Handled " + message.body());
    }
}
