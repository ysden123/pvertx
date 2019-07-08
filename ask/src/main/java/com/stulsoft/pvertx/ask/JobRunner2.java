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
import java.util.concurrent.Future;

/**
 * Sends message to <i>ServiceVerticle</i> and waits reply.
 *
 * @author Yuriy Stul
 */
class JobRunner2 {
    private static final Logger logger = LoggerFactory.getLogger(JobRunner2.class);

    private final Vertx vertx;

    JobRunner2(final Vertx vertx) {
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
        var result = CompletableFuture.supplyAsync(() -> {
            try {
//                var reply = ask().get();  // This operator blocks Vertx event loop!
//                return reply;
                ask();
                return new JsonObject().put("result", "Done in mock");
            } catch (Exception ex) {
                logger.error("Execute failed. " + ex.getMessage());
                return null;
            }
        });
        return result.get();
    }

    Future<JsonObject> ask() {
        logger.info("Asking ...");
        CompletableFuture<JsonObject> future = new CompletableFuture<>();

        var deliveryOptions = new DeliveryOptions()
                .setSendTimeout(5000);
        vertx.eventBus().send(ServiceVerticle.EB_ADDRESS,
                new JsonObject().put("data", "Do it"),
                deliveryOptions,
                askResult -> {
                    if (askResult.succeeded()) {
                        logger.info("ask succeeded");
                        future.complete((JsonObject) askResult.result().body());
                    } else {
                        logger.error("ask failed. {}", askResult.cause().getMessage());
                        future.completeExceptionally(askResult.cause());
                    }
                });

        return future;
    }
}
