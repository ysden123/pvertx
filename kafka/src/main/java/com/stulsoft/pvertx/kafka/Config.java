/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.kafka;

/**
 * @author Yuriy Stul
 */
public interface Config {
    static String kafkaUrl() {
        var url = System.getenv("KAFKA_URL");
        if (url == null)
            url = "localhost:9094";
        return url;
    }
}
