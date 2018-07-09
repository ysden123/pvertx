/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.file;

import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read file not line by line.
 *
 * @author Yuriy Stul
 */
public class Read1 {
    private static final Logger logger = LoggerFactory.getLogger(Read1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        final var path = "test-files";
        var vertx = Vertx.vertx();

        vertx.fileSystem().open(path + "/write1.txt", new OpenOptions()
                .setRead(true), openRes -> {
            if (openRes.succeeded()) {
                var file = openRes.result();
                var observable = file.toObservable();
                var d=observable
                        .subscribe(line -> logger.info(line.toString()),
                                t -> logger.error(t.getMessage(), t),
                                vertx::close);
            } else {
                logger.error(openRes.cause().getMessage(), openRes.cause());
                vertx.close();
            }
        });
        logger.info("<==main");
    }

}
