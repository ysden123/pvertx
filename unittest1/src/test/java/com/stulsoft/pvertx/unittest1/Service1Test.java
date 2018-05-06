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

import static org.junit.Assert.*;

@RunWith(VertxUnitRunner.class)
public class Service1Test {
    private static Logger logger = LoggerFactory.getLogger(Service1Test.class);
    private Vertx vertx;

    @Before
    public void setUp() {
        vertx = Vertx.vertx();

        vertx.deployVerticle(new Service1());
    }

    @After
    public void tearDown() {
        vertx.close();
    }

    @Test
    public void messageTest(TestContext context) {
        final Async async = context.async();
        vertx.eventBus().send("service1", "test", (ar) ->{
            if (ar.succeeded()){
                logger.debug("ar.result().headers(): {}", ar.result().headers().size() );

                logger.info("Reply is {}", ar.result().body());
                context.assertEquals("My reply", ar.result().body(), "Invalid reply from service");
                async.complete();
            }else{
                context.fail("Didn't receive reply");
            }
        } );
    }
}