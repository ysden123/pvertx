package com.stulsoft.pvertx.httpserver2;

import com.stulsoft.pvertx.httpserver2.verticles.HttpServer;
import com.stulsoft.pvertx.httpserver2.verticles.Service;
import com.stulsoft.pvertx.httpserver2.verticles.Service2;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 * @since 5/2/2018
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Started Main");
        Vertx vertx = Vertx.vertx();

        Verticle service = new Service();
        vertx.deployVerticle(service);

        Verticle service2 = new Service2();
        vertx.deployVerticle(service2);

        Verticle httpServer = new HttpServer();

        logger.info("Deploying HttpServer");
        vertx.deployVerticle(httpServer);

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();

        logger.info("Stopping Vertx...");
        vertx.close();
        logger.info("Stopped Main");
    }
}
