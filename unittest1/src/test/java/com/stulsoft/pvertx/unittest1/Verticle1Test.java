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
public class Verticle1Test {
    private static Logger logger = LoggerFactory.getLogger(Verticle1Test.class);
    private Vertx vertx;

    @Before
    public void setUp() throws Exception {
        logger.info("Initializing vertx...");
        vertx = Vertx.vertx();
    }

    @After
    public void tearDown() throws Exception {
        logger.info("Closing vertx...");
        vertx.close();
    }

    @Test
    public void start(TestContext context) {
        final Async async = context.async();
        vertx.deployVerticle(new Verticle1());
        vertx.setTimer(500, (l)->{
            async.complete();
        });
    }
}