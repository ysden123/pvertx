package com.stulsoft.pvertx.simple1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/1/2018
 */
public class HttpService extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(HttpService.class);
    @Override
    public void start(Promise<Void> startPromise){
        logger.info("Started HttpService");
        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise){
        logger.info("Stopped HttpService");
        stopPromise.complete();
    }
}
