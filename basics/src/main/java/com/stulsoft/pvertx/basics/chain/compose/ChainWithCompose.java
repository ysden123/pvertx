/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain.compose;

import com.stulsoft.pvertx.basics.Utils;
import com.stulsoft.pvertx.basics.chain.VerticleForChain;
import io.reactivex.Completable;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain with compose
 *
 * @author Yuriy Stul
 */
public class ChainWithCompose {
    private static final Logger logger = LoggerFactory.getLogger(ChainWithCompose.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.deployVerticle(VerticleForChain.class.getName(),
                ar -> test1(vertx).subscribe(vertx::close));
    }

    private static Completable test1(final Vertx vertx) {
        return Completable.create(source ->
                doWork(vertx, "test 1")
                        .compose(v -> doWork(vertx, "test 2"))
                        .onComplete(v -> source.onComplete()));
    }

    private static Future<Void> doWork(final Vertx vertx, String msg) {
        var promise = Promise.<Void>promise();
        vertx.eventBus()
                .request(VerticleForChain.EB_ADDRESS,
                        msg,
                        ar -> promise.complete());
        return promise.future();
    }
}
