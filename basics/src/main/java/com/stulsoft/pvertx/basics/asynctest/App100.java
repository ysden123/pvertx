/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.asynctest;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.stulsoft.pvertx.basics.Constants.ATTR_DATA;

/**
 * @author Yuriy Stul
 */
public class App100 {
    private static final Logger logger = LoggerFactory.getLogger(SyncVerticle.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        var deployOptions = new DeploymentOptions().setInstances(2);
        vertx.deployVerticle(new SyncVerticle());
        vertx.deployVerticle(AsyncVerticle.class.getName(), deployOptions);

        vertx.setTimer(100, l -> {
            var counter = 20;
            var lastData = String.format("data %d %s", counter, "processed");
            for (var i = 1; i <= counter; ++i) {
                var msg = new JsonObject().put(ATTR_DATA, "data " + i);
                logger.debug("Sending {}", msg.encode());
                vertx.eventBus().request(SyncVerticle.EB_ADDRESS, msg, rslt -> {
                    if (rslt.succeeded()) {
                        var replyData = ((JsonObject) rslt.result().body()).getString(ATTR_DATA);
                        logger.info("replyData={}", replyData);
                        if (lastData.equals(replyData)) {
                            vertx.close();
                        }
                    } else {
                        logger.error(rslt.cause().getMessage());
                    }
                });
            }
        });
    }
}
