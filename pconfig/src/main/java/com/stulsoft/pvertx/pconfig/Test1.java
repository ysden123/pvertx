package com.stulsoft.pvertx.pconfig;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
public class Test1 {
    private static Logger logger = LoggerFactory.getLogger(Test1.class);

    public static void main(String[] args) {
        logger.info("Started Test1");
        Vertx vertx = Vertx.vertx();

        ConfigManager.getInstance().load(vertx, ar -> {

            Verticle v1 = new Verticle1();

            vertx.deployVerticle(v1);
        });

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();

        vertx.close();
        logger.info("Finished Test1");
    }
}
