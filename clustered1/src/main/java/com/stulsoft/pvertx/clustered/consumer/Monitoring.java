/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.consumer;

import static com.stulsoft.pvertx.clustered.common.Config.*;

import java.util.logging.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class Monitoring extends AbstractVerticle {

    private final Logger logger = Logger.getLogger(Monitoring.class.getName());

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Reboot monitor started");
        vertx.eventBus().<JsonObject>consumer(REBOOT_ADDRESS, message -> {
            JsonObject reboot = message.body();
            logger.info(("Status " + reboot.getString(STATUS) + " by " + reboot.getString(BY)));
        });
        startFuture.complete();
    }

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions().setClustered(true);
        Vertx.clusteredVertx(vertxOptions, ar -> {
            if (ar.failed()) {
                System.err.println("Cannot create vert.x instance : " + ar.cause());
            } else {
                Vertx vertx = ar.result();
                vertx.deployVerticle(Monitoring.class.getName());
            }
        });
    }
}