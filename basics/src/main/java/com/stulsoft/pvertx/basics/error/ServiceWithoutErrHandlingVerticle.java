/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.error;

import com.stulsoft.pvertx.common.Utils;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ServiceWithoutErrHandlingVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServiceWithoutErrHandlingVerticle.class);

    private static final String EB_ADDRESS = ServiceWithoutErrHandlingVerticle.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<String> msg) {
        logger.debug("Handling {}", msg.body());
        doWork(msg)
                .subscribe(
                        (Consumer<String>) msg::reply
                );
    }

    private Single<String> doWork(Message<String> msg) {
        return Single.create(source -> {
            if (msg.body().toLowerCase().contains("error"))
                source.onError(new IllegalArgumentException("Test error for " + msg.body()));
            else
                source.onSuccess("Done for " + msg.body());
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        Utils
                .deployVerticles(vertx, new String[]{ServiceWithoutErrHandlingVerticle.class.getName()})
                .subscribe(() -> runRequest(vertx, "text 1")
                        .andThen(runRequest(vertx, "text 2 with error"))
                        .andThen(runRequest(vertx, "text 3"))
                        .subscribe(vertx::close));
    }

    private static Completable runRequest(Vertx vertx, String text) {
        return Completable.create(source -> vertx.eventBus()
                .<String>request(
                        EB_ADDRESS,
                        text,
                        ar -> {
                            if (ar.succeeded())
                                logger.debug("Succeeded {}", ar.result().body());
                            else
                                logger.error("Failed {}", ar.cause().getMessage());
                            source.onComplete();
                        }));
    }
}
