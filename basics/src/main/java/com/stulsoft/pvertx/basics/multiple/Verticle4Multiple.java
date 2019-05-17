/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.multiple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class Verticle4Multiple extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Verticle4Multiple.class);
    public static final String EB_ADDRESS = "Verticle4Multiple";
    private Random random = new Random();

    /**
     * Start the verticle.<p>
     * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.<p>
     * If your verticle does things in its startup which take some time then you can override this method
     * and call the startFuture some time later when start up is complete.
     *
     * @param startFuture a future which should be called when verticle start-up is complete.
     * @throws Exception
     */
    @Override
    public void start(Future<Void> startFuture){
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        startFuture.complete();
    }

    /**
     * Stop the verticle.<p>
     * This is called by Vert.x when the verticle instance is un-deployed. Don't call it yourself.<p>
     * If your verticle does things in its shut-down which take some time then you can override this method
     * and call the stopFuture some time later when clean-up is complete.
     *
     * @param stopFuture a future which should be called when verticle clean-up is complete.
     * @throws Exception
     */
    @Override
    public void stop(Future<Void> stopFuture){
        logger.info("==>stop");
        stopFuture.complete();
    }

    private void handler(Message<String> msg) {
        try {
            logger.info("Handling {}", msg.body());
//            Thread.sleep(random.nextInt(600));
            Thread.sleep(500);
            msg.reply("Done");
        } catch (Exception ignore) {
        }
    }

}
