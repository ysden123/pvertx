/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Yuriy Stul
 */
class MessageGeneratorTest {

    @Test
    void generator() throws Exception {
        var generator = new MessageGenerator(1000, 10);

        var count = new AtomicInteger(0);
        var countDownLatch = new CountDownLatch(1);
        generator.generate()
                .doOnComplete(countDownLatch::countDown)
                .subscribe(msg -> {
                    System.out.println("msg: " + msg);
                    count.incrementAndGet();
                });
        countDownLatch.await(1, TimeUnit.MINUTES);
        assertEquals(10, count.get());
    }
}