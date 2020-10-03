/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.unittest1;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chain with pause
 *
 * @author Yuriy Stul
 */
@RunWith(VertxUnitRunner.class)
public class ChainTest {
    private static final Logger logger = LoggerFactory.getLogger(ChainTest.class);
    private Vertx vertx;

    @Before
    public void setUp() {
        logger.info("==>setUp");
        vertx = Vertx.vertx();
    }

    @After
    public void tearDown() {
        logger.info("==>tearDown");
        vertx.close();
    }

    @Test
    public void test(TestContext context) {
        logger.info("==>test");
        Async async = context.async();
        Promise<Void> startPromise = Promise.promise();
        startPromise.future().onComplete(result -> {
            context.assertTrue(result.succeeded());
            async.complete();
        });

        chain1()
                .compose(v -> chain2())
                .compose(v -> pause())
                .compose(v -> {
                    startPromise.complete();
                    return startPromise.future();
                });
    }

    private Future<Void> chain1() {
        logger.info("==>chain1");
        Promise<Void> promise = Promise.promise();

        vertx.setTimer(1000, l -> {
            logger.info("Completed chain1");
            promise.complete();
        });

        return promise.future();
    }

    private Future<Void> chain2() {
        logger.info("==>chain2");
        Promise<Void> promise = Promise.promise();

        vertx.setTimer(1000, l -> {
            logger.info("Completed chain2");
            promise.complete();
        });

        return promise.future();
    }

    private Future<Void> pause() {
        logger.info("==>pause");
        Promise<Void> promise = Promise.promise();

        vertx.setTimer(3000, l -> {
            logger.info("Completed pause");
            promise.complete();
        });

        return promise.future();
    }


}
