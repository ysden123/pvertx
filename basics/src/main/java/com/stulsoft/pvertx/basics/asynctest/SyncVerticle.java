/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.asynctest;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class SyncVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SyncVerticle.class);
    public static final String EB_ADDRESS = SyncVerticle.class.getName();

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        super.start(startFuture);
        logger.info("Starting ...");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<JsonObject> msg) {
        vertx.executeBlocking(promise -> {
            try {
                var rslt = vertx.eventBus().rxRequest(AsyncVerticle.EB_ADDRESS, msg.body()).blockingGet();
                promise.complete(rslt.body());
            }catch(Exception ex){
                logger.error("aaa {}", ex.getMessage());
                promise.fail(ex.getMessage());
            }
        }, result -> {
            if (result.succeeded()) {
                msg.reply(result.result());
            } else {
                var errMsg=result.cause().getMessage();
                logger.error(errMsg);
                msg.fail(123, errMsg);
            }
        });
    }

    private void stub() {
        logger.info("==>stub");
    }


}
