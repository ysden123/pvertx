/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex3.app;


import com.stulsoft.pvertx.preactivex3.verticle.V1;
import com.stulsoft.pvertx.preactivex3.verticle.V2;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class App2 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(App2.class);
    private Single<String> d2;
    public final static String EB_ADDRESS = "app2";

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var d = vertx
                .rxDeployVerticle("com.stulsoft.pvertx.preactivex3.app.App2")
                .subscribe(s -> {
                    logger.info("success = [{}]", s);
                    vertx.eventBus().request(EB_ADDRESS, "go!", totalRes -> {
                        if (totalRes.succeeded()) {
                            logger.info("Total result [{}]", totalRes.result().body().toString());
                        } else {
                            logger.error(totalRes.cause().getMessage(), totalRes.cause());
                        }
                    });
                }, t -> logger.error("error = [{}]", t.getMessage()));
        vertx.setTimer(1000, l -> {
            d.dispose();
            vertx.close();
            logger.info("<==main");
        });
    }

    @Override
    public void start() {
        logger.info("==>start");

        vertx.eventBus().consumer(EB_ADDRESS, this::handler);

        Single<String> d1 = vertx.rxDeployVerticle("com.stulsoft.pvertx.preactivex3.verticle.V1");
        d2 = vertx.rxDeployVerticle("com.stulsoft.pvertx.preactivex3.verticle.V2");

        d1.subscribe(s1 -> d2.subscribe(s2 -> super.start()));
    }

    private void handler(final Message<String> message) {
        logger.info("==>handler with message {}", message.body());
        var m1 = vertx
                .eventBus()
                .rxRequest(V1.EB_ADDRESS, "do d1");
        var m2 = vertx
                .eventBus()
                .rxRequest(V2.EB_ADDRESS, "do d2");
        m1.subscribe(r1 -> {
            logger.info("Response from d1 [{}]", r1.body().toString());
            m2.subscribe(r2 -> {
                logger.info("Response from d2 [{}]", r2.body().toString());
                message.reply("Done");
            });
        });
    }
}
