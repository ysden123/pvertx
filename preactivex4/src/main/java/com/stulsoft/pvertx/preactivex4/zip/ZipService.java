/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.preactivex4.zip;

import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuriy Stul
 */
public class ZipService extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(ZipService.class);

    public static String EB_ADDRESS = ZipService.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    private void handler(Message<String> message) {
        logger.info("==>handler");

        if (message.body().contains("2"))
            message.fail(123, "Error for " + message.body());
        else
            message.reply("Done for " + message.body());
    }

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Vertx.vertx();

        vertx.rxDeployVerticle(new ZipService())
                .subscribe(__ -> {
                    List<Single<Message<String>>> replyList = new ArrayList<>();
                    for (int i = 1; i <= 3; ++i) {
                        replyList.add(vertx.eventBus().<String>rxRequest(EB_ADDRESS, "msg " + i));
                    }

                    Single.zip(replyList, replyArray -> {
                        var reply = new JsonObject();
                        logger.debug("replyArray.length={}", replyArray.length);
                        for(int i = 0; i < replyArray.length; ++i){
                            var reply1 = (Message<String>) replyArray[i];
                            logger.debug("reply1={}",reply1.body());
                        }
                        return reply;
                    })
                            .subscribe(result -> {
                                        logger.debug("P 001");
                                        vertx.close();
                                    },
                                    error -> {
                                        logger.error("(2) {}", error.getMessage());
                                        vertx.close();
                                    });
                });
    }
}
