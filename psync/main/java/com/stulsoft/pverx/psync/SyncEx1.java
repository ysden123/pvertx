/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pverx.psync;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.Promise;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Yuriy Stul
 */
public class SyncEx1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SyncEx1.class);

    public static final String EB_ADDRESS = SyncEx1.class.getName();

    private static Vertx theVertx;

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        logger.info("Starting ServiceVerticle ...");
        theVertx = vertx;
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    private void handler(Message<String> message) {
        logger.info("==>handler");
        message.reply("Handled " + message.body());
    }

    public static String doJob() {
        logger.info("==>doJob");

        var result = CompletableFuture.supplyAsync(() -> {
            return sync();
        });

        try {
            return result.get().get();
        }catch(Exception exception){
            logger.error(exception.getMessage());
            return null;
        }
    }

    private static Future<String> sync() {
        logger.info("==>sync");
        CompletableFuture<String> future = new CompletableFuture<>();
        theVertx.eventBus().<String>rxRequest(EB_ADDRESS, "test")
                .subscribe(result -> {
                    future.complete(result.body());
                });
        return future;
    }


    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        vertx.deployVerticle(new SyncEx1(), dr -> {
            logger.info("Deployed");
            logger.info(doJob());
        });
    }
}
