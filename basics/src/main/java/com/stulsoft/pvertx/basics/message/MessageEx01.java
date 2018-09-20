/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.message;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates creation of the Message objects for unit testing.
 *
 * @author Yuriy Stul
 */
public class MessageEx01 {
    private static final Logger logger = LoggerFactory.getLogger(MessageEx01.class);

    static class MyMessage extends Message<JsonObject> {
        private final JsonObject json;

        MyMessage(JsonObject json) {
            super(null);
            this.json = json;
        }

        @Override
        public JsonObject body() {
            return json;
        }
    }

    public static void main(String[] args) {
        logger.info("==>main");
        test1();
        logger.info("<==main");
    }

    private static void test1() {
        logger.info("==>test1");
        var json = new JsonObject().put("attr1", "111@@@222");
        var msg = new MyMessage(json);
        logger.info(msg.body().toString());
        logger.info("<==test1");
    }
}
