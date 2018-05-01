package com.stulsoft.pvertx.simple1;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

/**
 * @author Yuriy Stul
 * @since 5/1/2018
 */
public class Runner {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Verticle httpService = new HttpService();

        vertx.deployVerticle(httpService);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vertx.close();
    }
}
