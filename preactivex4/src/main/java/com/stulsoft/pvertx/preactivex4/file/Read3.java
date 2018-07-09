/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex4.file;

import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads file line by line
 *
 * @author Yuriy Stul
 */
public class Read3 {
    private static final Logger logger = LoggerFactory.getLogger(Read3.class);

    public static void main(String[] args) {
        logger.info("==>main");
        final var path = "test-files";
        var vertx = Vertx.vertx();

        vertx.fileSystem().open(path + "/write1.txt", new OpenOptions()
                .setRead(true), openRes -> {
            if (openRes.succeeded()) {
                var recordParser = RecordParser
                        .newDelimited(System.getProperty("line.separator"));
                var d = recordParser.toObservable().subscribe(buffer -> logger.info(buffer.toString()));
                var file = openRes.result();
                file.handler(recordParser)
                        .endHandler(v -> {
                            file.close();
                            d.dispose();
                            vertx.close();
                        });
            } else {
                logger.error(openRes.cause().getMessage(), openRes.cause());
                vertx.close();
            }
        });
        logger.info("<==main");
    }

}
