/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.webclient;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class WebClientEx1 {
    private static final Logger logger = LoggerFactory.getLogger(WebClientEx1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx=Vertx.vertx();
        var wce=new WebClientEx1();
        wce.test1(vertx);
        vertx.setTimer(3000,l->{
            vertx.close();
            logger.info("<==main");
        });
    }

    void test1(Vertx vertx) {
        logger.info("==>test1");
        var client = WebClient.create(vertx, new WebClientOptions());

        client.get(80, "http://www.yahoo.com", "")
                .timeout(10000)   // success
//                .timeout(300)   // timeout exception
                .send(ar->{
                    if (ar.succeeded())
                        logger.info("Succeeded");
                    else
                        logger.error(ar.cause().getMessage());
                });
    }

}
