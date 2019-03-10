/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pfuture2;

import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 */
public class Main2 {
    private static Logger logger = LoggerFactory.getLogger(Main2.class);

    public static void main(String[] args) {
        logger.info("Started Main1");
        Future<String> result1 = f1();
        result1.setHandler(ar -> logger.info("Result: {}", ar.result()));

        System.out.println("For end enter some line");
        Scanner sc1 = new Scanner(System.in);
        sc1.next();
        sc1.close();

        logger.info("Stopped Main2");
    }

    private static Future<String> f1() {
        logger.info("Started f1");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignore) {
        }

        return Future.succeededFuture("Success");
    }
}
