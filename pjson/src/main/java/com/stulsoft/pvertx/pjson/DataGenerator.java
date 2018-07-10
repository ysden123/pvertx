/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pjson;

import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class DataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    private static final String path = "test-files";

    private DataGenerator() {
    }

    static void buildSimpleJson(final Vertx vertx, final String fileName, final int size) {
        mkPath(vertx);
        vertx.fileSystem()
                .open(path + "/" + fileName,
                        new OpenOptions()
                                .setTruncateExisting(true)
                                .setCreate(true)
                                .setWrite(true),
                        openRes -> {
                            if (openRes.succeeded()) {
                                var file = openRes.result();
                                file.write(Buffer.buffer("{"));
                                for (var i = 1; i < size; ++i) {
                                    file.write(Buffer.buffer(String.format("\"key%d\":\"value %d\",%n", i, i)));
                                }
                                file.write(Buffer.buffer(String.format("\"key%d\":\"value %d\"%n", size, size)));
                                file.write(Buffer.buffer("}"));
                                file.end();
                            } else {
                                logger.error(openRes.cause().getMessage(), openRes.cause());
                            }
                        });
    }

    private static void mkPath(final Vertx vertx) {
        if (!vertx.fileSystem().existsBlocking(path))
            vertx.fileSystem().mkdirsBlocking(path);
    }
}
