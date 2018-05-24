/*
 * Created by Yuriy Stul 24 May 2018
 */
package com.stulsoft.pvertx.bus.object;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

/**
 * @author Yuriy Stul
 *
 */
@RunWith(VertxUnitRunner.class)
public class MyVerticleTest {
	private static Logger logger = LoggerFactory.getLogger(MyVerticle.class);

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
		vertx.deployVerticle(new MyVerticle(), ar -> {
			if (ar.succeeded()) {
				for (int i = 1; i <= 3; ++i) {
					MyObject obj = new MyObject("name " + i, i);
					vertx.eventBus().send(MyVerticle.ADDRESS, JsonObject.mapFrom(obj));
				}

			}
		});
		vertx.setTimer(500, (l) -> {
			async.complete();
		});
	}
}
