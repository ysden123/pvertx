/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.scheduler;

import io.reactivex.Observable;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class Scheduler1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler1.class);
    private static final String EB_ADDRESS = "schedule1";

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.preactivex4.scheduler.Scheduler1",
                depRes -> {
                    logger.info("Deployed {}", depRes.result());

                    vertx.eventBus().request(EB_ADDRESS, "go!", response -> {
                        logger.info("Result is {}", response.result().body().toString());
                        vertx.close();
                    });
                });
        logger.info("<==main");
    }

    @Override
    public void start() throws Exception {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("<==stop");
        super.stop();
    }

    private void handler(final Message<String> message) {
        logger.info("==>handler with {}", message.body());
        var scheduler = RxHelper.scheduler(context);
        Observable<Long> timer = Observable.interval(500, 500, TimeUnit.MILLISECONDS, scheduler);
        timer.subscribe(l -> {
            logger.info("l={}", l);
            if (l >= 5) {
                scheduler.shutdown();
                message.reply("Done!");
            }
        });
    }
}
