/*
 * Created by Yuriy Stul 23 May 2018
 */
package com.stulsoft.pvertx.worker1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

/**
 * @author Yuriy Stul
 *
 */
public class Service extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(Service.class);
	public static final String SERVICE_ADDRESS = "service_address";

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start() throws Exception {
		super.start();
		logger.info("Starting Service.");

		vertx.eventBus().consumer(SERVICE_ADDRESS, m -> {
			logger.info("Received message");
			vertx.setTimer(500, l -> {
				logger.info("Handled message");
				m.reply("Done");
			});
		});
	}

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#stop()
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		logger.info("Stopping Service");
	}

}
