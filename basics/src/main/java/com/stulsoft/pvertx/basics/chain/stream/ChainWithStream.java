/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain.stream;

import com.stulsoft.pvertx.basics.Utils;
import com.stulsoft.pvertx.basics.chain.VerticleForChain;
import io.reactivex.Completable;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain with ambWith
 *
 * @author Yuriy Stul
 */
public class ChainWithStream {
    private static final Logger logger = LoggerFactory.getLogger(ChainWithStream.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.deployVerticle(VerticleForChain.class.getName(),
                ar -> test1(vertx)
                        .subscribe(vertx::close));
    }

    // using ambWith
    private static Completable test1(final Vertx vertx) {
        return Completable.create(source -> {
            var rxVertx = io.vertx.reactivex.core.Vertx.newInstance(vertx);

            rxVertx.eventBus()
                    .rxRequest(VerticleForChain.EB_ADDRESS, "test 1 1")
                    .ambWith(rxVertx.eventBus()
                            .rxRequest(VerticleForChain.EB_ADDRESS, "test 1 2"))
                    .subscribe(v -> source.onComplete());
        });
    }
}
