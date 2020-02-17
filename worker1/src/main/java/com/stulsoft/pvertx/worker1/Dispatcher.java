/*
 * Created by Yuriy Stul 23 May 2018
 */
package com.stulsoft.pvertx.worker1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

/**
 * @author Yuriy Stul
 */
public class Dispatcher extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    public static final String DISPATCHER_ADDRESS = "dispatcher_asddress";

    /* (non-Javadoc)
     * @see io.vertx.core.AbstractVerticle#start()
     */
    @Override
    public void start() throws Exception {
        super.start();
        logger.info("Starting Dispatcher.");

        vertx.eventBus().consumer(DISPATCHER_ADDRESS, m ->
                vertx.eventBus().request(Service.SERVICE_ADDRESS, "message", ar -> {
                    logger.info("Received response \"{}\" from Service", ar.result().body());
                    m.reply(ar.result().body());
                }));
    }

    /* (non-Javadoc)
     * @see io.vertx.core.AbstractVerticle#stop()
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Stopping Dispatcher.");
    }

}
