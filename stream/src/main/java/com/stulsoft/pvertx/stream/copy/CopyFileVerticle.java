/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.core.Promise;
import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.file.AsyncFile;
import io.vertx.reactivex.core.streams.Pump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class CopyFileVerticle extends AbstractVerticle {
    private final static Logger logger = LoggerFactory.getLogger(CopyFileVerticle.class);

    public static final String EB_ADDRESS = "copy.file.verticle";

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting {} ...", getClass().getSimpleName());
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        logger.info("<==start");
        startPromise.complete();
    }

    /**
     * If your verticle has simple synchronous clean-up tasks to complete then override this method and put your clean-up
     * code in here.
     *
     * @throws Exception if occurred any problem
     */
    @Override
    public void stop() throws Exception {
        logger.info("Stopping {} ...", getClass().getSimpleName());
        super.stop();
    }

    private void handler(final Message<CopyRequest> msg) {
        logger.info("Handling [{}] request", msg.body().toString());
        logger.debug("msg.body().srcFileName(): {}", msg.body().srcFileName());
        var fs = vertx.fileSystem();

        if (!fs.existsBlocking("results"))
            fs.mkdirsBlocking("results");

        AsyncFile[] asyncReadFile = {null};
        fs.rxOpen(msg.body().srcFileName(), new OpenOptions().setCreate(false).setRead(true))
                .flatMap(theAsyncReadFile -> {
                    logger.info("Opened file to read");
                    asyncReadFile[0] = theAsyncReadFile;
                    return fs.rxOpen("results/" + msg.body().dstFileName(),
                            new OpenOptions().setCreate(true).setWrite(true));
                })
                .subscribe(
                        asyncWriteFile -> {
                            asyncReadFile[0].endHandler((Void) -> {
                                logger.info("Source file ended");
                                asyncReadFile[0].close();
                                logger.info("Replying Done...");
                                msg.reply("Done");
                            });
                            asyncReadFile[0].exceptionHandler(error -> {
                                asyncReadFile[0].close();
                                logger.error("Failed copying with error [{}]", error.getMessage());
                                msg.fail(123, error.getMessage());
                            });
                            Pump.pump(asyncReadFile[0], asyncWriteFile).start();
                        },
                        error -> {
                            logger.error("Failed copying with error [{}]", error.getMessage());
                            msg.fail(123, error.getMessage());
                        });
    }
}
