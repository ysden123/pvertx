/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.delivery;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.eventbus.DeliveryOptions;

/**
 * ServiceForDelivery is very slow service. Timeout 3sec is not enough to receive reply.
 * However the service successfully handled request and did its job.
 *
 * @author Yuriy Stul
 */
public class App123 {
    public static void main(String[] args) {
        var vertx = Utils.createVertx();

        vertx.deployVerticle(
                ServiceForDelivery.class.getName(),
                r -> vertx.setTimer(12,
                        l -> {
                            System.out.println("Sending request...");
                            vertx
                                    .eventBus()
                                    .<String>request(ServiceForDelivery.EB_ADDRESS, "Test 123",
                                            new DeliveryOptions().setSendTimeout(3_000L),
                                            dr -> {
                                                if (dr.succeeded()) {
                                                    System.out.println("Response: " + dr.result().body());
                                                } else {
                                                    System.out.println("Error: " + dr.cause().getMessage());
                                                }
                                            });
                        }));

        vertx.setTimer(6_000L, l -> vertx.close());
    }
}
