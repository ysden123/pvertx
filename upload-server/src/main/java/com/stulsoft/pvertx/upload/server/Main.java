/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.upload.server;

import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Runs ServerVertice
 *
 * @author Yuriy Stul
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("==>main");

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(ServerVerticle.class.getName());

        terminate(vertx);

        logger.info("<==main");
    }

    private static void terminate(final Vertx vertx) {
        System.out.println("For end enter any line, e.g. empty line...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();

        vertx.close();
    }
}
