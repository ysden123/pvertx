/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.blocking;

import io.reactivex.Single;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


/**
 * @author Yuriy Stul
 */
public class EBServiceWithFlatMapVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(EBServiceWithFlatMapVerticle.class);

    public static final String EB_ADDRESS = EBServiceWithFlatMapVerticle.class.getName();

    private final Random random = new Random(System.currentTimeMillis());

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<String> msg) {
        process1(msg.body())
                .flatMap(s -> process2(s))
                .flatMap(s -> process3(s))
                .subscribe(r -> msg.reply(r));
    }

    private Single<String> process1(String msg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> {
                    logger.info("Handling {}", msg);
                    source.onSuccess(msg + " process1");
                }));
    }

    private Single<String> process2(String msg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> {
                    logger.info("Handling {}", msg);
                    source.onSuccess(msg + " process2");
                }));
    }

    private Single<String> process3(String msg) {
        return Single.create(source -> vertx.setTimer(123 + random.nextInt(1000),
                l -> {
                    logger.info("Handling {}", msg);
                    source.onSuccess(msg + " process3");
                }));
    }
}
