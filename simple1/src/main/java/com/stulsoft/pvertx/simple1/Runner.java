package com.stulsoft.pvertx.simple1;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/1/2018
 */
public class Runner {
    private static Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        logger.info("start");
        Vertx vertx = Vertx.vertx();
        Verticle httpService = new HttpService();

        vertx.deployVerticle(httpService);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vertx.close();
        logger.info("end");
    }
}
