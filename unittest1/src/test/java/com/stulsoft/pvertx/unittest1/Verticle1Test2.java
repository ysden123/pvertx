package com.stulsoft.pvertx.unittest1; /**
 * @author Yuriy Stul
 * @since 5/6/2018
 */

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

@RunWith(VertxUnitRunner.class)
public class Verticle1Test2 {
    private static Logger logger = LoggerFactory.getLogger(Verticle1Test2.class);
    private Vertx vertx;

    @Before
    public void setUp(TestContext context) throws Exception {
        logger.info("Initializing vertx...");
        vertx = Vertx.vertx();

        vertx.deployVerticle(new Verticle1());
    }

    @After
    public void tearDown() throws Exception {
        logger.info("Closing vertx...");
        vertx.close();
    }

    @Test
    public void test1(TestContext context){
        final Async async = context.async();
        logger.info("(1) Current Async.count={}", async.count());
        vertx.setTimer(500, (l)->{
            async.complete();
            logger.info("(2) Current Async.count={}", async.count());
        });
    }

    @Test
    public void test2(TestContext context){
        final Async async = context.async();
        logger.info("(1) Current Async.count={}", async.count());
        vertx.setTimer(500, (l)->{
            async.countDown();
            logger.info("(2) Current Async.count={}", async.count());
        });
    }

    @Test
    public void test21(TestContext context){
        final Async async = context.async(3);
        logger.info("(1) Current Async.count={}", async.count());
        vertx.setTimer(500, (l)->{
            async.countDown();
            logger.info("(2) Current Async.count={}", async.count());
            async.countDown();
            logger.info("(3) Current Async.count={}", async.count());
            async.countDown();
            logger.info("(4) Current Async.count={}", async.count());
        });
    }

/* Error: timeout
    @Test(timeout = 100)
    public void test3(TestContext context){
        final Async async = context.async();
        logger.info("(1) Current Async.count={}", async.count());
    }
*/
}