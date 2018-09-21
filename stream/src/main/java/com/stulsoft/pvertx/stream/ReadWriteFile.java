/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream;

import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.streams.Pump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ReadWriteFile {
    private final static Logger logger = LoggerFactory.getLogger(ReadWriteFile.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Vertx.vertx();
        var fs = vertx.fileSystem();

        if (!fs.existsBlocking("results"))
            fs.mkdirsBlocking("results");

        fs.open("text1.txt", new OpenOptions().setRead(true), openReadResult -> {
            if (openReadResult.succeeded()) {
                logger.info("Opened file to read");
                var asyncReadFile = openReadResult.result();
                fs.open("results/text1Copy.txt",
                        new OpenOptions().setCreate(true).setWrite(true),
                        openWriteResult -> {
                            if (openWriteResult.succeeded()) {
                                logger.info("Opened file to write");
                                var asyncWriteFile = openWriteResult.result();
                                var pump = Pump.pump(asyncReadFile, asyncWriteFile);
                                pump.start();
                            } else {
                                logger.error(openWriteResult.cause().getMessage());
                            }
                        });
                asyncReadFile
                        .toObservable()
                        .doOnComplete(() -> {
                            asyncReadFile.close();
                            vertx.close();
                        })
                        .subscribe();


//                asyncReadFile.close();
            } else {
                logger.error(openReadResult.cause().getMessage());
            }
//            vertx.close();
        });
        logger.info("<==main");
    }
}
