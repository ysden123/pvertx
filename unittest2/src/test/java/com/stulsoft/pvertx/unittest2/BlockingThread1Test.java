/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.unittest2;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
@ExtendWith(VertxExtension.class)
public class BlockingThread1Test {
    private static final Logger logger = LoggerFactory.getLogger(BlockingThread1Test.class);

    @Test
    void test1(Vertx vertx, VertxTestContext testContext) {
        var eb = vertx.eventBus();
        var address = "test_address_1";

        eb.consumer(address, msg -> {
            vertx.setTimer(1000, l -> {
                msg.reply("Done");
            });
        });

        eb.send(address, "Do it 1", response -> {
            testContext.completeNow();
        });
    }
}
