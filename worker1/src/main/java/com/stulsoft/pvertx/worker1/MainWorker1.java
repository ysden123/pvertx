/*
 * Created by Yuriy Stul 23 May 2018
 */
package com.stulsoft.pvertx.worker1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

/**
 * @author Yuriy Stul
 *
 */
public class MainWorker1 {
	private static Logger logger = LoggerFactory.getLogger(MainWorker1.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Started MainWorker1 on {}-{} thread", Thread.currentThread().getName(),
				Thread.currentThread().getId());
		Vertx vertx = Vertx.vertx();
		Verticle service = new Service();

		vertx.deployVerticle(service);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vertx.close();
		logger.info("Stopped MainWorker1 on {}-{} thread", Thread.currentThread().getName(),
				Thread.currentThread().getId());
	}

}
