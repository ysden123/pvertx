/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pasyncfile;

import io.vertx.core.file.OpenOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Writes many buffers in many write operations
 *
 * @author Yuriy Stul
 */
public class WriteFile2 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(WriteFile2.class);

    private static final String EB_ADDRESS_START = "WriteFile2_start";
    private static final String EB_ADDRESS_STOP = "WriteFile2_stop";

    private int dataCount = 100;

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.pasyncfile.WriteFile2", depRes ->
                vertx.eventBus().request(EB_ADDRESS_START, "start", execResult ->
                        vertx.eventBus().send(EB_ADDRESS_STOP, "stop")));
    }

    @Override
    public void start() throws Exception {
        logger.info("==>start");
        vertx.eventBus().consumer(EB_ADDRESS_START, this::execute);
        vertx.eventBus().consumer(EB_ADDRESS_STOP, r -> vertx.close());
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("==>stop");
        super.stop();
    }

    private void execute(Message<String> message) {
        logger.info("==>execute {}", message.body());

        var path = "out";
        if (!vertx.fileSystem().existsBlocking(path))
            vertx.fileSystem().mkdirBlocking(path);

        vertx.fileSystem().open(path + "/file2.txt",
                new OpenOptions()
                        .setCreate(true)
                        .setAppend(false)
                        .setWrite(true),
                openRes -> {
                    var outputFile = openRes.result(); // AsyncFile

                    Optional<Buffer> optBuffer;
                    while ((optBuffer = nextData()).isPresent()) {
                        outputFile.write(optBuffer.get());
                    }

                    outputFile.end();
                    message.reply("done");
                });
    }

    private Optional<Buffer> nextData() {
        return (--dataCount > 0) ?
                Optional.of(Buffer.buffer(String.format("Line # %d%n", dataCount)))
                :
                Optional.empty();
    }
}
