/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DeploymentOptions - setWorker(true)
 *
 * @author Yuriy Stul
 */
public class EBServiceRunner3 {
    private static final Logger logger = LoggerFactory.getLogger(EBServiceRunner3.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        var scanner = new Scanner(System.in);
        System.out.println("Choose (enter number:)");
        System.out.println("0 - exit");
        System.out.println("1 - executeBlocking, random time");
        System.out.println("2 - executeBlocking, constant time");
        System.out.println("3 - no executeBlocking, random time");
        System.out.println("4 - no executeBlocking, constant time");
        var response = scanner.nextInt();
        switch (response) {
            case 1:
                test1(vertx);
                break;
            case 2:
                test2(vertx);
                break;
            case 3:
                test3(vertx);
                break;
            case 4:
                test4(vertx);
                break;
            default:
                vertx.close();
        }
    }

    private static void test1(final Vertx vertx) {
        vertx.deployVerticle(EBServiceVerticle1.class.getName(), deploymentOptions(), dr -> {
            var count = new AtomicInteger(0);
            for (int i = 1; i <= 10; ++i) {
                vertx.eventBus()
                        .request(
                                EBServiceVerticle1.EB_ADDRESS_1, "test " + i,
                                result -> {
//                                    logger.info("Result {}", result.result().body());
                                    if (count.incrementAndGet() >= 10) {
                                        vertx.close();
                                    }
                                });
            }
        });
    }

    private static void test2(final Vertx vertx) {
        vertx.deployVerticle(EBServiceVerticle1.class.getName(), deploymentOptions(), dr -> {
            var count = new AtomicInteger(0);
            for (int i = 1; i <= 10; ++i) {
                vertx.eventBus()
                        .request(
                                EBServiceVerticle1.EB_ADDRESS_2, "test " + i,
                                result -> {
//                                    logger.info("Result {}", result.result().body());
                                    if (count.incrementAndGet() >= 10) {
                                        vertx.close();
                                    }
                                });
            }
        });
    }

    private static void test3(final Vertx vertx) {
        vertx.deployVerticle(EBServiceVerticle1.class.getName(), deploymentOptions(), dr -> {
            var count = new AtomicInteger(0);
            for (int i = 1; i <= 10; ++i) {
                vertx.eventBus()
                        .request(
                                EBServiceVerticle1.EB_ADDRESS_3, "test " + i,
                                result -> {
//                                    logger.info("Result {}", result.result().body());
                                    if (count.incrementAndGet() >= 10) {
                                        vertx.close();
                                    }
                                });
            }
        });
    }

    private static void test4(final Vertx vertx) {
        vertx.deployVerticle(EBServiceVerticle1.class.getName(), deploymentOptions(), dr -> {
            var count = new AtomicInteger(0);
            for (int i = 1; i <= 10; ++i) {
                vertx.eventBus()
                        .request(
                                EBServiceVerticle1.EB_ADDRESS_4, "test " + i,
                                result -> {
//                                    logger.info("Result {}", result.result().body());
                                    if (count.incrementAndGet() >= 10) {
                                        vertx.close();
                                    }
                                });
            }
        });
    }

    private static DeploymentOptions deploymentOptions() {
        return new DeploymentOptions()
                .setWorker(true);
    }
}
