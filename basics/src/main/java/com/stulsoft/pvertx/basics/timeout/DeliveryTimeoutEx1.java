/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.timeout;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playing with delivery options
 *
 * @author Yuriy Stul
 */
public class DeliveryTimeoutEx1 {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryTimeoutEx1.class);

    private static final String EB_ADDRESS1 = "address1";
    private static final String EB_ADDRESS2 = "address2";

    // Timeout exception
    private static void test1(Vertx vertx) {
        logger.info("==>test1");
        var eb = vertx.eventBus();
        eb.consumer(EB_ADDRESS1, msg -> {
            vertx.setTimer(1000, l -> msg.reply("Done 1"));
        });

        var deliveryOptions = new DeliveryOptions()
                .setSendTimeout(500);

        eb.send(EB_ADDRESS1, "Do it", deliveryOptions, result -> {
            if (result.succeeded()) {
                logger.info("Success {}", result.result().body());
            } else {
                logger.error("Failure. {}", result.cause().getMessage());
            }
        });
        logger.info("<==test1");
    }

    // Timeout exception
    private static void test2(Vertx vertx) {
        logger.info("==>test2");
        var eb = vertx.eventBus();
        eb.consumer(EB_ADDRESS2, msg -> {
            vertx.setTimer(1000, l -> msg.reply("Done 2"));
        });

        var deliveryOptions = new DeliveryOptions()
                .setSendTimeout(1500);

        eb.send(EB_ADDRESS2, "Do it", deliveryOptions, result -> {
            if (result.succeeded()) {
                logger.info("Success {}", result.result().body());
            } else {
                logger.error("Failure. {}", result.cause().getMessage());
            }
        });
        logger.info("<==test2");
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        test1(vertx);
        test2(vertx);
        logger.info("<==main");
    }
}
