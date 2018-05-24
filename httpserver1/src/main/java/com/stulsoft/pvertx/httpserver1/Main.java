package com.stulsoft.pvertx.httpserver1;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stulsoft.pvertx.httpserver1.verticles.HttpServer;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

/**
 * @author Yuriy Stul
 * @since 5/2/2018
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Started Main");
        Vertx vertx = Vertx.vertx();

        Verticle httpServer = new HttpServer();

        logger.info("Deploying HttpServer");
        vertx.deployVerticle(httpServer);

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();
        sc.close();

        logger.info("Stopping Vertx...");
        vertx.close();
        logger.info("Stopped Main");
    }
}
