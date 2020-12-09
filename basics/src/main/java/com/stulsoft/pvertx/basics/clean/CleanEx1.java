/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.clean;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class CleanEx1 {
    private static final Logger logger = LoggerFactory.getLogger(CleanEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();
        vertx.eventBus().registerDefaultCodec(CleanMessage.class, new CleanMessageCodec());

        vertx.deployVerticle(CleanService.class.getName(), dr -> {
            var jsonObj = new JsonObject()
                    .put("a1", 123);
            var msg = new CleanMessage(jsonObj);
            var param1 = new JsonObject[]{null};
            param1[0] = new JsonObject()
                    .put("p1", "it is p1");
            msg.setParam1(param1[0]);

            vertx.eventBus().<String>request(CleanService.EB_ADDRESS, msg, ar -> {
                param1[0] = null;
                logger.info("Response: {}", ar.result().body());
                vertx.close();
            });
        });
    }
}
