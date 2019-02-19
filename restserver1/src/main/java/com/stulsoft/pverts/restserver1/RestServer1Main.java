/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pverts.restserver1;

import com.stulsoft.pverts.restserver1.verticles.RestServer;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 */
public class RestServer1Main {
    private static Logger logger = LoggerFactory.getLogger(RestServer1Main.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();

        logger.info("Deploying restServer...");
        vertx.deployVerticle(RestServer.class.getName());

/*
        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();
        sc.close();

        logger.info("Stopping Vertx...");
        vertx.close();
        logger.info("<==main");
*/
    }
}
