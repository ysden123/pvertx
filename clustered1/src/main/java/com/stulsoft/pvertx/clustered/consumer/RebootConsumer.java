/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.consumer;

import static com.stulsoft.pvertx.clustered.common.Config.*;

import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class RebootConsumer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(RebootConsumer.class);
    private boolean reboot = false;
    private String id = "ID-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Reboot verticle " + id + " started");
        vertx.eventBus().<Integer>consumer("ids", message -> {
            int id = message.body();

            if (id == 0) {
                launchReboot();
            }
        });

        startFuture.complete();
    }

    private void launchReboot() {
        if (!reboot) {
            vertx.sharedData().getLock("lock", ar -> {
                if (ar.succeeded()) {
                    Lock lock = ar.result();
                    reboot = true;
                    vertx.eventBus().send(REBOOT_ADDRESS, startRebootMessage());
                    logger.info(">> Start system reboot ... ");

                    vertx.setTimer(3000, h -> {
                        vertx.eventBus().send(REBOOT_ADDRESS, endRebootMessage());
                        logger.info("<< Reboot Over");
                        reboot = false;
                        lock.release();
                    });

                } else {
                    logger.error("Should work !", ar.cause());
                }
            });
        }
    }

    private JsonObject startRebootMessage() {
        JsonObject message = new JsonObject();
        message.put(STATUS, "STARTED");
        message.put(BY, id);
        return message;
    }

    private JsonObject endRebootMessage() {
        JsonObject message = new JsonObject();
        message.put(STATUS, "FINISHED");
        message.put(BY, id);
        return message;
    }

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions().setClustered(true);
        Vertx.clusteredVertx(vertxOptions, ar -> {
            if (ar.failed()) {
                System.err.println("Cannot create vert.x instance : " + ar.cause());
            } else {
                Vertx vertx = ar.result();
                vertx.deployVerticle(RebootConsumer.class.getName());
            }
        });
    }
}
