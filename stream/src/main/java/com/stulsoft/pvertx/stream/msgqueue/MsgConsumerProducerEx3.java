/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.msgqueue;

import com.stulsoft.pvertx.common.Utils;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message producer and message consumer with pause, resume, writeQueueFull, and drainHandler
 *
 * @author Yuriy Stul
 */
public class MsgConsumerProducerEx3 {
    private static final Logger logger = LoggerFactory.getLogger(MsgConsumerProducerEx3.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var msgQueueAddress = "msgQueueAddress";

        var vertx = Utils.createVertx();

        MessageProducer<String> msgProducer = vertx.eventBus().sender(msgQueueAddress);
        msgProducer.setWriteQueueMaxSize(6);    // Limitation on message queue

        MessageConsumer<String> msgConsumer = vertx.eventBus().consumer(msgQueueAddress);
        // Imitation of long time message processing
        msgConsumer.handler(handler -> {
            logger.info("Received {}", handler.body());
            vertx.setTimer(1000,
                    l -> logger.info("Handled {}", handler.body()));
        });

        var recordParser = RecordParser.newDelimited("\n");
        recordParser.handler(buffer -> {
            logger.info("writing {}", buffer.toString());
            msgProducer.write(buffer.toString());   // Add a message to queue for following processing
            if (msgProducer.writeQueueFull()) {     // Check whether queue is full
                recordParser.pause();               // Pause getting source messages
                logger.info("recordParser is paused");
                msgProducer.drainHandler(h -> {     // We are here when queue is not full
                    recordParser.resume();          // Restore getting ource message
                    logger.info("recordParser is resumed");
                });
            }
        });

        // Imitation an "endless" message source
        vertx.fileSystem().open(
                "text2.txt",
                new OpenOptions(),
                ar -> {
                    if (ar.succeeded()) {
                        AsyncFile asyncFile = ar.result();
                        asyncFile.handler(recordParser);
                    } else {
                        logger.error("Failed open text2.txt file: {}", ar.cause().getMessage());
                    }
                });
    }
}
