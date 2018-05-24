/*
 * Created by Yuriy Stul 24 May 2018
 */
package com.stulsoft.pvertx.bus.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 *
 */
public class MyVerticle extends AbstractVerticle {
	public static final String ADDRESS = "MyVerticle";
	private static final Logger logger = LoggerFactory.getLogger(MyVerticle.class);

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start() throws Exception {
		super.start();
		logger.info("Starting MyVerticle");
		vertx.eventBus().consumer(ADDRESS, this::handleMessage);
	}

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#stop()
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		logger.info("Sttopped MyVerticle");
	}

	private void handleMessage(Message<JsonObject> message) {
		MyObject obj =  message.body().mapTo(MyObject.class);
		logger.info("Received message {}", obj);
	}
}
