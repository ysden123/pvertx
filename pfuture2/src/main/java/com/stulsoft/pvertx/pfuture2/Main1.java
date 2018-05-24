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
public class Main1 {
    private static Logger logger = LoggerFactory.getLogger(Main1.class);

    public static void main(String[] args) {
        logger.info("Started Main1");
        Future<String> result1 = f1();

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();
        sc.close();

        logger.info("Result: {}", result1.result());


        logger.info("Stopped Main1");
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
