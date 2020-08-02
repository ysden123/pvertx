/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import io.reactivex.Completable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class MainVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start() throws Exception {
        super.start();
        logger.info("Starting send messages...");
        var generator = new MessageGenerator(1000);
        generator.generate().subscribe(s -> vertx
                .eventBus()
                .<Message<String>>request(
                        MessageProcessorVerticle.EB_ADDRESS,
                        s,
                        ar -> {
                            logger.info("Result: {}", ar.result().body());
                        }));

    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var mv = new MainVerticle();
        mv.deployVerticles(vertx).subscribe(() -> logger.info("Deployed verticles"));
        logger.info("<==main");
    }

    private Completable deployVerticles(final Vertx vertx) {
        return Completable.create(source -> {
            vertx.deployVerticle(MessageProcessorVerticle.class.getName(),
                    dr -> {
                        vertx.deployVerticle(MainVerticle.class.getName(), dr2 -> source.onComplete());
                    });
        });
    }
}
