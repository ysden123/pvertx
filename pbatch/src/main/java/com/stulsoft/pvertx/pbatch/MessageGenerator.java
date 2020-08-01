/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;

/**
 * @author Yuriy Stul
 */
public class MessageGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MessageGenerator.class);

    private final Random random = new Random(System.currentTimeMillis());

    private final int maxDelay;
    private final int maxCount;

    public MessageGenerator(int maxDelay, int maxCount) {
        this.maxDelay = maxDelay;
        this.maxCount = maxCount;
    }

    public MessageGenerator(int maxDelay) {
        this(maxDelay, 0);
    }

    public Flowable<String> generate() {
        return Flowable.create(
                source -> {
                    var executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        var count = 0L;
                        for (; ; ) {
                            try {
                                Thread.sleep(10 + random.nextInt(maxDelay));
                            } catch (Exception ignore) {

                            }
                            if (!source.isCancelled()) {
                                source.onNext("Message # " + ++count);
                            }
                            if (maxCount > 0 && (count >= maxCount))
                                break;
                        }

                        if (!source.isCancelled()) {
                            source.onComplete();
                        }
                    });
                },
                BackpressureStrategy.BUFFER);
    }
}
