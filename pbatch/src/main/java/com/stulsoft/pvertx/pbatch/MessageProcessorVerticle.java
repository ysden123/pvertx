/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import io.reactivex.Completable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author Yuriy Stul
 */
public class MessageProcessorVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessorVerticle.class);

    public static final String EB_ADDRESS = MessageProcessorVerticle.class.getName();

    private final LinkedList<Message<String>> queue = new LinkedList<>();
    private static final int maxQueueSize = 3;
    private final Random random = new Random(System.currentTimeMillis());

    @Override
    public void start() throws Exception {
        logger.info("Starting...");
        super.start();
//        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        vertx.eventBus().consumer(EB_ADDRESS, this::handlerSync);
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping...");
        super.stop();
    }

    private void handlerSync(final Message<String> msg) {
        makeWork(msg).subscribe(()-> msg.reply("Handled " + msg.body()));
    }

    private Completable makeWork(final Message<String> msg) {
        return Completable.create(
                source -> vertx.setTimer(
                        123 + random.nextInt(1000),
                        l -> {
                            logger.info("Processing {}", msg.body());
                            msg.reply("Done for " + msg.body());
                            source.onComplete();
                        }));
    }
}
