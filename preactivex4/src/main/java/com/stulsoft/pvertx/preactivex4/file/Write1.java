/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.file;

import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Write1 {
    private static final Logger logger = LoggerFactory.getLogger(Write1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        final var path="test-files";
        var vertx = Vertx.vertx();
        if (!vertx.fileSystem().existsBlocking(path)) {
            vertx.fileSystem().mkdirBlocking(path);
        }
        vertx.fileSystem().open(path +"/write1.txt", new OpenOptions()
                .setWrite(true)
                .setCreate(true), openRes -> {
            if (openRes.succeeded()) {
                var file = openRes.result();
                for (int i = 1; i <= 100; ++i) {
                    file.write(Buffer.buffer(String.format("Line # %d%n", i)));
                }
                file.end();
            } else {
                logger.error(openRes.cause().getMessage(), openRes.cause());
            }
            vertx.close();
        });
        logger.info("<==main");
    }
}
