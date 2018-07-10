/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pjson;

import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.JsonEventType;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.parsetools.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yuriy Stul
 */
public class Parser2 {
    private static final Logger logger = LoggerFactory.getLogger(Parser2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        DataGenerator
                .buildJsonArray(vertx, "data2.json", 10000)
                .compose(v -> readFile(vertx));
    }

    private static Future<Void> readFile(final Vertx vertx) {
        Future<Void> future = Future.future();

        vertx.fileSystem().open("test-files/data2.json",
                new OpenOptions()
                , openRes -> {
                    var file = openRes.result();
                    var counter = new AtomicInteger();
                    var jsonParser = JsonParser.newParser(file);
                    jsonParser.objectValueMode()
                            .exceptionHandler(t -> {
                                logger.error(t.getMessage(), t);
                                file.close();
                                future.fail(t);
                            })
                            .endHandler(v -> {
                                logger.info("Processed {} records", counter.get());
                                file.close();
                                vertx.close();
                                future.complete();
                            })
                            .handler(event -> {
                                if (event.type() == JsonEventType.VALUE) {
                                    if (counter.incrementAndGet() <= 10) {
                                        logger.info("{}->{}", event.fieldName(), event.value().toString());
                                    }
                                } else {
                                    logger.info("{}", event.type());
                                }
                            });
                });

        return future;
    }

}
