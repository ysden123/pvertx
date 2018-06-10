/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.unittest1;

import io.vertx.core.Future;
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
    public void tearDown(TestContext context) {
        logger.info("==>tearDown");
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void test(TestContext context) {
        logger.info("==>test");
        Async async = context.async();
        Future<Void> startFuture = Future.future();
        startFuture.setHandler(result -> {
            context.assertTrue(result.succeeded());
            async.complete();
        });

        chain1()
                .compose(v -> chain2())
                .compose(v -> pause())
                .compose(v -> startFuture.complete(), startFuture);
    }

    private Future<Void> chain1() {
        logger.info("==>chain1");
        Future<Void> future = Future.future();

        vertx.setTimer(1000, l -> {
            logger.info("Completed chain1");
            future.complete();
        });

        return future;
    }

    private Future<Void> chain2() {
        logger.info("==>chain2");
        Future<Void> future = Future.future();

        vertx.setTimer(1000, l -> {
            logger.info("Completed chain2");
            future.complete();
        });

        return future;
    }

    private Future<Void> pause() {
        logger.info("==>pause");
        Future<Void> future = Future.future();

        vertx.setTimer(3000, l -> {
            logger.info("Completed pause");
            future.complete();
        });

        return future;
    }


}
