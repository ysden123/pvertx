/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pqueue;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Yuriy Stul
 */
public class ReceiveVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveVerticle.class);

    public static String EB_ADDRESS = "ReceiveVerticle";

    private AtomicBoolean inProcess = new AtomicBoolean(false);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.info("Starting {}", ReceiveVerticle.class.getSimpleName());
        vertx.eventBus().consumer(EB_ADDRESS, this::handleInputMsg);
        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        logger.info("Stopping {}", ReceiveVerticle.class.getSimpleName());
        stopFuture.complete();
    }

    private void handleInputMsg(Message<String> msg) {
        boolean done = false;
        for (var attempt = 1; attempt <= 5; ++attempt) {
            var delay = inProcess.get() ? 1000 : 100;
            vertx.setTimer(delay, l -> {
                if (!inProcess.get()) {
                    inProcess.set(true);
                    logger.info("Handling [{}] message", msg.body());
                    done = true;
                    break;
                }

            });
            if (!inProcess.get()) {
                inProcess.set(true);
                logger.info("Handling [{}] message", msg.body());
                done = true;
                break;
            }
        }

        long timerID = vertx.setPeriodic(1000, new Handler<Long>() {
            @Override
            public void handle(Long event) {
//                vertx.cancelTimer(timerID);
            }
        });
        if (done) {
            msg.reply("Done");
        } else {
            msg.fail(1, "Time out");
        }
    }

    private Flowable<String> queue() {
        return Flowable.create(source -> {
            source.onNext("next value");
                },
                BackpressureStrategy.BUFFER);
    }
}
