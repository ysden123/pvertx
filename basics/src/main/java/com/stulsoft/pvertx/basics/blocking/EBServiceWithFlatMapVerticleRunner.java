/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import com.stulsoft.pvertx.common.Utils;
import io.reactivex.Completable;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yuriy Stul
 */
public class EBServiceWithFlatMapVerticleRunner {
    private static final Logger logger = LoggerFactory.getLogger(EBServiceWithFlatMapVerticleRunner.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();

        vertx.deployVerticle(EBServiceWithFlatMapVerticle.class.getName(),
                dr -> {
                    test1(vertx)
                            .subscribe(() -> vertx.close());
                });
    }

    private static Completable test1(Vertx vertx) {
        return Completable.create(source -> {
            var count = new AtomicInteger(0);
            int n = 5;
            for (int i = 1; i <= n; ++i) {
                vertx.eventBus().request(EBServiceWithFlatMapVerticle.EB_ADDRESS
                        , "test " + i,
                        ar -> {
                            logger.info("Response: {}", ar.result().body().toString());
                            if (count.incrementAndGet() == n)
                                source.onComplete();
                        });

            }

        });
    }
}
