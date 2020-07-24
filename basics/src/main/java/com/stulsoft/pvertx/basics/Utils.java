/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;

/**
 * @author Yuriy Stul
 */
public interface Utils {
    static Vertx createVertx() {
        return Vertx.vertx(new VertxOptions()
                .setEventBusOptions(new EventBusOptions()
                        .setClustered(false)
                        .setIdleTimeout(0))
                .setMaxEventLoopExecuteTime(2000000000000L)
                .setMaxWorkerExecuteTime(60000000000000L)
                .setBlockedThreadCheckInterval(1000000));
    }
}
