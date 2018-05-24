package com.stulsoft.pvertx.pfuture;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
public class Main1 {
    private static Logger logger = LoggerFactory.getLogger(Main1.class);

    private static Future<String> foo() {
        Future<String> future = Future.future();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        future.tryComplete("Good");
//        future.tryFail("Bad");

        return future;

    }

    public static void main(String[] args) {
        logger.info("Start");
        Vertx vertx = Vertx.vertx();
        logger.info("Call foo");
        Future<String> result = foo();

        result.setHandler(ar->{
           if (ar.succeeded()){
               logger.info("Result: {}", ar.result());
           }else{
               logger.error("Error: {}", ar.cause().getMessage());
           }
        });


        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();
        sc.close();

        vertx.close();
        logger.info("Finish");
    }
}
