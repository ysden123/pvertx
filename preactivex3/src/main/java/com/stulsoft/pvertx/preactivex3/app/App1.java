/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex3.app;


import com.stulsoft.pvertx.preactivex3.verticle.V1;
import com.stulsoft.pvertx.preactivex3.verticle.V2;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class App1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(App1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var d = vertx
                .rxDeployVerticle("com.stulsoft.pvertx.preactivex3.app.App1")
                .subscribe(s -> logger.info("success = [{}]", s),
                        t -> logger.error("error = [{}]", t.getMessage()));
        vertx.setTimer(1000, l -> {
            d.dispose();
            vertx.close();
            logger.info("<==main");
        });
    }

    @Override
    public void start() throws Exception {
        logger.info("==>start");
        var d1 = vertx.rxDeployVerticle("com.stulsoft.pvertx.preactivex3.verticle.V1");
        var d2 = vertx.rxDeployVerticle("com.stulsoft.pvertx.preactivex3.verticle.V2");
        d1.subscribe(s -> {
            logger.info("d1 success = [{}]", s);
            vertx.eventBus().rxRequest(V1.EB_ADDRESS, "").subscribe(response -> {
                logger.info("d1.result=[{}]", response.body().toString());
            });
        });
        d2.subscribe(s -> {
            logger.info("d2 success = [{}]", s);
            vertx.eventBus().rxRequest(V2.EB_ADDRESS, "").subscribe(response -> {
                logger.info("d2.result=[{}]", response.body().toString());
            });
        });
        super.start();
    }
}
