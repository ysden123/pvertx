/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain.andthen;

import com.stulsoft.pvertx.common.Utils;
import com.stulsoft.pvertx.basics.chain.VerticleForChain;
import io.reactivex.Completable;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain with andThen
 *
 * @author Yuriy Stul
 */
public class ChainWithAndThen {
    private static final Logger logger = LoggerFactory.getLogger(ChainWithAndThen.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.deployVerticle(VerticleForChain.class.getName(),
                ar -> test1(vertx).subscribe(vertx::close));
    }

    private static Completable test1(final Vertx vertx) {
        return Completable.create(source -> {
            doWork(vertx, "test 1")
                    .andThen(doWork(vertx, "test 2"))
                    .subscribe(source::onComplete);
        });
    }

    private static Completable doWork(final Vertx vertx, String msg) {
        return Completable.create(source -> vertx.eventBus()
                .request(VerticleForChain.EB_ADDRESS,
                        msg,
                        ar -> source.onComplete()));
    }
}
