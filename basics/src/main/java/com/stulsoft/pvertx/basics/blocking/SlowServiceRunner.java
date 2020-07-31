/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import io.reactivex.Completable;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SlowServiceRunner {
    private static final Logger logger = LoggerFactory.getLogger(SlowServiceRunner.class);

    private final Vertx vertx;

    public SlowServiceRunner(Vertx vertx) {
        this.vertx = vertx;
    }

    public Completable blocking() {
        return Completable.create(source -> vertx.eventBus()
                .request(
                        SlowService.EB_ADDRESS_BLOCKING,
                        "test blocking",
                        ar -> source.onComplete()));
    }

    public Completable noBlocking() {
        return Completable.create(source -> vertx.eventBus()
                .request(
                        SlowService.EB_ADDRESS_NO_BLOCKING,
                        "test no blocking",
                        ar -> source.onComplete()));
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(SlowService.class.getName(), dr -> {
            var runner = new SlowServiceRunner(vertx);

            runner.blocking()
                    .andThen(runner.blocking())
                    .andThen(runner.noBlocking())
                    .andThen(runner.noBlocking())
                    .subscribe(() -> {
                        logger.info("<==main");
                        vertx.close();
                    });
        });
    }
}
