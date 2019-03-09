/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.pconfig3;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Utils {
    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    public static void showConfig(JsonObject config) {
        config
                .fieldNames()
                .stream()
                .sorted()
                .forEach(key -> logger.info("{} = {}", key, config.getValue(key).toString()));
    }
}
