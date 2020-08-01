/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import io.reactivex.Completable;
import io.vertx.core.AbstractVerticle;
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
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping...");
        super.stop();
    }

    private void handler(final Message<String> msg) {
        queue.add(msg);
        if (queue.size() >= maxQueueSize) {
            var works = new Completable[queue.size()];
            for (int i = 0; i < queue.size(); ++i) {
                works[i] = makeWorkAsync(queue.get(i));
            }
            queue.clear();
//            Completable.concatArray(works).subscribe(() -> logger.info("Handled {} messages", works.length));
            Completable.concatArray(works)
                    .doOnComplete(() -> logger.info("Handled {} messages", works.length))
                    .blockingGet();
        }
    }

    private Completable makeWorkAsync(Message<String> msg) {
        return Completable.create(
                source -> vertx.setTimer(
                        123 + random.nextInt(1000),
                        l -> {
                            logger.info("Processing {}", msg.body());
                            msg.reply("Done for " + msg.body());
                            source.onComplete();
                        }));
    }

    private Completable makeWorkSync(Message<String> msg) {
        return Completable.create(
                source -> {
                    try {
                        Thread.sleep(123 + random.nextInt(1000));
                        logger.info("Processing {}", msg.body());
                        msg.reply("Done for " + msg.body());
                        source.onComplete();
                    } catch (Exception ignore) {

                    }
                });
    }
}
