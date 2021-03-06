/*
 * Created by Yuriy Stul 23 May 2018
 */
package com.stulsoft.pvertx.worker1;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * @author Yuriy Stul
 */
public class MainWorker1 {
    private static Logger logger = LoggerFactory.getLogger(MainWorker1.class);

    public static void main(String[] args) {
        logger.info("Started MainWorker1");
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("com.stulsoft.pvertx.worker1.Dispatcher");

        Random random = new Random(53456);
        DeploymentOptions options = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle("com.stulsoft.pvertx.worker1.Service", options, ar -> {
            for (int i = 1; i <= 5; ++i) {
                final int id = i;
                vertx.setTimer(100 + random.nextInt(500), l ->
                        vertx.eventBus().request(Dispatcher.DISPATCHER_ADDRESS, "message", ar2 ->
                                logger.info("({}) Received completed result {}", id, ar2.result().body())));
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vertx.close();
        logger.info("Stopped MainWorker1.");
    }

}
