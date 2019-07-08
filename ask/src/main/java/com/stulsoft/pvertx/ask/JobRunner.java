/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.ask;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Sends message to <i>ServiceVerticle</i> and waits reply.
 *
 * @author Yuriy Stul
 */
class JobRunner {
    private static final Logger logger = LoggerFactory.getLogger(JobRunner.class);

    private final Vertx vertx;

    JobRunner(final Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Sends message to <i>ServiceVerticle</i> and waits reply.
     *
     * @return execution result
     * @throws InterruptedException was interrupted
     * @throws ExecutionException   execution failed
     */
    JsonObject execute() throws InterruptedException, ExecutionException {
        logger.info("Starting execute method ...");
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Executing job ...");
            var deliveryOptions = new DeliveryOptions()
                    .setSendTimeout(5000);

            try {
                return (JsonObject) vertx.eventBus().rxSend(ServiceVerticle.EB_ADDRESS,
                        new JsonObject().put("data", "Do it"),
                        deliveryOptions)
                        .blockingGet()  // BLOCKS Vertx thread!!!
                        .body();
            } catch (Exception ex) {
                logger.error("Job failed. {}", ex.getMessage());
                return null;
            }
        }).get();


//        return CompletableFuture.<JsonObject>supplyAsync(() -> {
//            try {
//                Thread.sleep(1000);
//                return new JsonObject().put("result", "Job done 1");
//            } catch (InterruptedException ex) {
//                return null;
//            }
//        }).get();
    }
}
