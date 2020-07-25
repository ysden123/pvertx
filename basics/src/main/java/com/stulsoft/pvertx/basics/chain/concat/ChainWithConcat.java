/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain.concat;

import com.stulsoft.pvertx.basics.Utils;
import com.stulsoft.pvertx.basics.chain.VerticleForChain;
import io.reactivex.Completable;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain with concat
 *
 * @author Yuriy Stul
 */
public class ChainWithConcat {
    private static final Logger logger = LoggerFactory.getLogger(ChainWithConcat.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.deployVerticle(VerticleForChain.class.getName(),
                ar -> test1(vertx).subscribe(vertx::close));
    }

    private static Completable test1(final Vertx vertx) {
        return Completable.create(source ->
                Completable.concatArray(
                        doWork(vertx, "test 1"),
                        doWork(vertx, "test 2")
                )
                        .subscribe(() -> source.onComplete()));
    }

    private static Completable doWork(final Vertx vertx, String msg) {
        return Completable.create(source -> vertx.eventBus()
                .request(VerticleForChain.EB_ADDRESS,
                        msg,
                        ar -> source.onComplete()));
    }
}
