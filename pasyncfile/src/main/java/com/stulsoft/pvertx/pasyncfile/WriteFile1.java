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

/**
 * Writes one buffer in one write operation
 *
 * @author Yuriy Stul
 */
public class WriteFile1 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(WriteFile1.class);

    private static final String EB_ADDRESS_START = "WriteFile1_start";
    private static final String EB_ADDRESS_STOP = "WriteFile1_stop";

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.pasyncfile.WriteFile1", depRes ->
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

        vertx.fileSystem().open(path + "/file1.txt", new OpenOptions(), openRes -> {
            var outputFile = openRes.result(); // AsyncFile
            var buffer = generateData();
            outputFile.write(buffer, 0, writeRes -> message.reply("done"));
        });

    }

    private Buffer generateData() {
        var buffer = Buffer.buffer();
        for (var i = 1; i <= 100; ++i) {
            buffer.appendString(String.format("Line # %d%n", i));
        }
        return buffer;
    }
}
