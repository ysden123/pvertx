/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pmongo.util;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public interface Utils {
    static Vertx createVertx() {
        return Vertx.vertx(new VertxOptions()
                .setEventBusOptions(new EventBusOptions()
                        .setIdleTimeout(0))
                .setMaxEventLoopExecuteTime(2000000000000L)
                .setMaxWorkerExecuteTime(60000000000000L)
                .setBlockedThreadCheckInterval(1000000));
    }

    static JsonObject mongoConfig() {
        return new JsonObject()
                .put("connection_string", "mongodb://root:admin@localhost:27017")
                .put("db_name", "pmongo");
    }
}
