/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pjson;

import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class Parser1 {
    private static final Logger logger = LoggerFactory.getLogger(Parser1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        DataGenerator.buildSimpleJson(vertx, "data1.json", 10);
        vertx.setTimer(2000, l->vertx.close());
        logger.info("<==main");
    }
}
