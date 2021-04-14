/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pverts.restserver1;

import com.stulsoft.pverts.restserver1.verticles.RestServer2;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class RestServer2Main {
    private static Logger logger = LoggerFactory.getLogger(RestServer2Main.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();

        logger.info("Deploying restServer2...");
        vertx.deployVerticle(RestServer2.class.getName());
    }
}
