/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * No order
 *
 * @author Yuriy Stul
 */
public class EBServiceVerticle1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(EBServiceVerticle1.class);

    public static String EB_ADDRESS_1 = EBServiceVerticle1.class.getName() + ".1";
    public static String EB_ADDRESS_2 = EBServiceVerticle1.class.getName() + ".2";
    public static String EB_ADDRESS_3 = EBServiceVerticle1.class.getName() + ".3";
    public static String EB_ADDRESS_4 = EBServiceVerticle1.class.getName() + ".4";

    private final Random random = new Random();

    @Override
    public void start() throws Exception {
        super.start();

        vertx.eventBus().consumer(EB_ADDRESS_1, this::handler1);
        vertx.eventBus().consumer(EB_ADDRESS_2, this::handler2);
        vertx.eventBus().consumer(EB_ADDRESS_3, this::handler3);
        vertx.eventBus().consumer(EB_ADDRESS_4, this::handler4);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void handler1(Message<String> msg) {
        vertx.executeBlocking(
                future -> {
//                    logger.info("Handling {}", msg.body());
                    vertx.setTimer(123 + random.nextInt(1000), l -> future.complete("Done for " + msg.body()));
                },
                result -> {
                    logger.info("Handled {}", msg.body());
                    if (result.succeeded()) {
                        msg.reply(result.result());
                    }
                });
    }

    private void handler2(Message<String> msg) {
        vertx.executeBlocking(
                future -> {
//                    logger.info("Handling {}", msg.body());
                    vertx.setTimer(1000, l -> future.complete("Done for " + msg.body()));
                },
                result -> {
                    logger.info("Handled {}", msg.body());
                    if (result.succeeded()) {
                        msg.reply(result.result());
                    }
                });
    }

    private void handler3(Message<String> msg) {
//        logger.info("Handling {}", msg.body());
        vertx.setTimer(123 + random.nextInt(1000), l -> {
            logger.info("Handled {}", msg.body());
            msg.reply("Done for " + msg.body());
        });
    }

    private void handler4(Message<String> msg) {
//        logger.info("Handling {}", msg.body());
        vertx.setTimer(1000, l -> {
            logger.info("Handled {}", msg.body());
            msg.reply("Done for " + msg.body());
        });
    }
}
