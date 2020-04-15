/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


/**
 * @author Yuriy Stul
 */
public class OneAfterOne {
    private static final Logger logger = LoggerFactory.getLogger(OneAfterOne.class);
    private final int chainLength = 3;

    private Future<Void> future;

    private final Vertx vertx;

    OneAfterOne(final Vertx vertx) {
        Objects.requireNonNull(vertx, "vertx cannot be null");
        this.vertx = vertx;
    }

    private Future<Void> runChain() {
        logger.info("==>runChain");
        var promise = Promise.<Void>promise();

        vertx.setTimer(100, l -> {
            chainFunction(1)
                    .compose(s2 -> chainFunction(2)
                            .compose(s3 -> chainFunction(3)))
                    .onComplete(ar -> {
                        logger.info("<==runChain");
                        promise.complete();
                    });
        });

        return promise.future();
    }

    private Future<String> chainFunction(int i) {
        logger.info("==>chainFunction");
        var promise = Promise.<String>promise();
        vertx.setTimer(5000, l -> {
            logger.debug("i={}", i);
            promise.complete("Done");
        });
        return promise.future();
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var t = new OneAfterOne(vertx);
        t.runChain().onComplete(ar -> {
            vertx.close();
            logger.info("<==main");
        });
    }
}
