/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.unittest2;


import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author Yuriy Stul
 */
@ExtendWith(VertxExtension.class)
public class BlockingThread1Test {
    @Test
    void test1(Vertx vertx, VertxTestContext testContext) {
        var eb = vertx.eventBus();
        var address = "test_address_1";

        eb.consumer(address, msg -> vertx.setTimer(1000, l -> msg.reply("Done")));

        eb.request(address, "Do it 1", response -> testContext.completeNow());
    }
}
