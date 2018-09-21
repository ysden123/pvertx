/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.streams.Pump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class CopyFileVerticle extends AbstractVerticle {
    private final static Logger logger = LoggerFactory.getLogger(CopyFileVerticle.class);

    public static final String EB_ADDRESS = "copy.file.verticle";

    /**
     * If your verticle does a simple, synchronous start-up then override this method and put your start-up
     * code in here.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        logger.info("Starting {} ...", getClass().getSimpleName());
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        super.start();
    }

    /**
     * If your verticle has simple synchronous clean-up tasks to complete then override this method and put your clean-up
     * code in here.
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        logger.info("Stopping {} ...", getClass().getSimpleName());
        super.stop();
    }

    private void handler(final Message<JsonObject> msg) {
        logger.info("Handling [{}] request", msg.body().toString());
        var fs = vertx.fileSystem();

        if (!fs.existsBlocking("results"))
            fs.mkdirsBlocking("results");

        fs.open(msg.body().getString("srcFileName"),
                new OpenOptions()
                        .setRead(true),
                openReadResult -> {
                    if (openReadResult.succeeded()) {
                        logger.info("Opened file to read");
                        var asyncReadFile = openReadResult.result();
                        fs.open("results/" + msg.body().getString("dstFileName"),
                                new OpenOptions().setCreate(true).setWrite(true),
                                openWriteResult -> {
                                    if (openWriteResult.succeeded()) {
                                        logger.info("Opened file to write");
                                        var asyncWriteFile = openWriteResult.result();
                                        var pump = Pump.pump(asyncReadFile, asyncWriteFile);
                                        pump.start();
                                    } else {
                                        logger.error(openWriteResult.cause().getMessage());
                                        msg.fail(123, openWriteResult.cause().getMessage());
                                    }
                                });
                        asyncReadFile.endHandler((Void)->{
                            logger.info("Source file ended");
                            asyncReadFile.close();
                            logger.info("Replying Done...");
                            msg.reply("Done");
                        });
                    } else {
                        logger.error(openReadResult.cause().getMessage());
                        msg.fail(123, openReadResult.cause().getMessage());
                    }
                });
    }
}
