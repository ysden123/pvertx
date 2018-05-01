package com.stulsoft.pvertx.simple1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * @author Yuriy Stul
 * @since 5/1/2018
 */
public class HttpService extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture){
        System.out.println("Started HttpService");
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture){
        System.out.println("Stopped HttpService");
        stopFuture.complete();
    }
}
