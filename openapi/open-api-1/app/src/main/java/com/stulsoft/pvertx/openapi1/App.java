/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.openapi1;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(new RoutingVerticle(), ar->{
           if (ar.succeeded()){
               logger.debug("Deployed");
           }else {
               logger.error("Failed the deploying");
           }
        });
    }
}
