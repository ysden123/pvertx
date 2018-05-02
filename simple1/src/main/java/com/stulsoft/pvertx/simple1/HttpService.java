package com.stulsoft.pvertx.simple1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/1/2018
 */
public class HttpService extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(HttpService.class);
    @Override
    public void start(Future<Void> startFuture){
        logger.info("Started HttpService");
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture){
        logger.info("Stopped HttpService");
        stopFuture.complete();
    }
}
