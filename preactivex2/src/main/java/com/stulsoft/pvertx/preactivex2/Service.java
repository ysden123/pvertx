/*
 * Created by Yuriy Stul 24 May 2018
 */
package com.stulsoft.pvertx.preactivex2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.rxjava.core.AbstractVerticle;

/**
 * @author Yuriy Stul
 *
 */
public class Service extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(Service.class);

	/* (non-Javadoc)
	 * @see io.vertx.rxjava.core.AbstractVerticle#init(io.vertx.core.Vertx, io.vertx.core.Context)
	 */
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		logger.info("Initializing Service...");
		vertx.eventBus().consumer("address", message -> {
			logger.info("Service received message {}", message.body());
			message.reply("Response from Service");
		});
	}

}
