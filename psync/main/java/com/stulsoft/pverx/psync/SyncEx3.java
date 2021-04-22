/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pverx.psync;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
//import io.vertx.reactivex.core.AbstractVerticle;
//import io.vertx.reactivex.core.Vertx;
//import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuriy Stul
 */
public class SyncEx3 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SyncEx3.class);

    public static final String EB_ADDRESS = SyncEx3.class.getName();

    private static Vertx theVertx;

    @Override
    public void start(Promise<Void> startFuture){
        logger.info("Starting ServiceVerticle ...");
        theVertx = vertx;
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    private void handler(Message<String> message) {
        logger.info("==>handler");
        message.reply("Handled " + message.body());
    }

    public static String doJob(String text) {
        logger.info("==>doJob");
        var result = CompletableFuture.supplyAsync(() -> {
            CompletableFuture<String> future = new CompletableFuture<>();
            theVertx.eventBus().<String>request(EB_ADDRESS, text)
                    .onComplete(jobResult -> {
                        future.complete(jobResult.result().body());
                    });
            return future;
        });

        try {
            return result.get(3, TimeUnit.SECONDS).get();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.deployVerticle(new SyncEx3(), dr -> {
            logger.info("Deployed");
            logger.info(doJob("test1"));
            logger.info(doJob("test2"));
            logger.info(doJob("test3"));
            vertx.close();
        });
    }
}
