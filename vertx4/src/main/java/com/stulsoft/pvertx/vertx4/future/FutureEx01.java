/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.vertx4.future;

import com.stulsoft.pvertx.common.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class FutureEx01 {
    private static final Logger logger = LoggerFactory.getLogger(Service2Verticle.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Utils.createVertx();

        var deploy = vertx.deployVerticle(new Service1Verticle())
                .compose(__ -> vertx.deployVerticle(new Service2Verticle()));

        deploy.onComplete(deployResult -> {
            if (deployResult.succeeded()) {
                logger.info("Succeeded deployment: {}", deployResult.result());
                var future1 = vertx.eventBus()
                        .<String>request(Service1Verticle.EB_ADDRESS, "Do work № 1");
                var future2 = future1.compose(result1 -> vertx.eventBus()
                        .<String>request(Service2Verticle.EB_ADDRESS, result1.body()));

                future2.onComplete(result ->{
                   if (result.succeeded()){
                       logger.info("Total result: {}", result.result().body());
                   }else{
                       logger.error(result.cause().getMessage());
                   }
                   vertx.close();
                });
            }
        });
    }
}
