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

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start() throws Exception {
		logger.info("Starting Service on {}-{} thread", Thread.currentThread().getName(), Thread.currentThread().getId());
		super.start();
	}

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#stop()
	 */
	@Override
	public void stop() throws Exception {
		logger.info("Stopping Service on {}-{} thread", Thread.currentThread().getName(), Thread.currentThread().getId());
		super.stop();
	}

	
}
