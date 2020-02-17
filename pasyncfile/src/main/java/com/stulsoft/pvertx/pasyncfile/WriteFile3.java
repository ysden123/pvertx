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

import java.util.stream.IntStream;

/**
 * Writes many buffers in many write operations
 *
 * @author Yuriy Stul
 */
public class WriteFile3 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(WriteFile3.class);

    private static final String EB_ADDRESS_START = "WriteFile3_start";
    private static final String EB_ADDRESS_STOP = "WriteFile3_stop";

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.pasyncfile.WriteFile3", depRes ->
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

        vertx.fileSystem().open(path + "/file3.txt",
                new OpenOptions()
                        .setCreate(true)
                        .setAppend(false)
                        .setWrite(true),
                openRes -> {
                    var outputFile = openRes.result(); // AsyncFile
                    IntStream.range(1, 101).forEach(i ->
                            outputFile.write(Buffer.buffer(String.format("line # %d%n", i)))
                    );
                    outputFile.end();
                    message.reply("done");
                });
    }
}
