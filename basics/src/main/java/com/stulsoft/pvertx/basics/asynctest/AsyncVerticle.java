/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.asynctest;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.stulsoft.pvertx.basics.Constants.ATTR_DATA;

/**
 * @author Yuriy Stul
 */
public class AsyncVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(AsyncVerticle.class);
    public static final String EB_ADDRESS = AsyncVerticle.class.getName();
    private final Random random = new Random();

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        super.start(startFuture);
        logger.info("Starting...");
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<JsonObject> msg) {
        try {
            var data = msg.body().getString(ATTR_DATA);
            logger.info("Processing {}", data);
            vertx.setTimer(random.nextInt(1000) + 1, l -> {
                /*if ("data 12".equals(data)) {
                    vertx.setTimer(30000, ll -> {
                        logger.info("Processed {}", data);
                        var reply = new JsonObject().put(ATTR_DATA, data + " processed");
                        try {
                            msg.reply(reply);
                        } catch (Exception ex) {
                            logger.error("reply ex: {}", ex.getMessage());
                            msg.fail(321, "cannot reply");
                        }
                    });
                } else*/
                if ("data 10".equals(data)) {
                    logger.debug("fail");
                    msg.fail(1, "timeout");
                } else {
                    logger.info("Processed {}", data);
                    var reply = new JsonObject().put(ATTR_DATA, data + " processed");
                    msg.reply(reply);
                }
            });
        } catch (Exception ex) {
            logger.error("222 {}", ex.getMessage());
        }
    }
}
