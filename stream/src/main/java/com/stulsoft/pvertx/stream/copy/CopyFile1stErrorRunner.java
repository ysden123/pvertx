/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Chained use the deployVerticle and rxSend.
 *
 * 1st request fails.
 *
 * @author Yuriy Stul
 */
public class CopyFile1stErrorRunner {
    private final static Logger logger = LoggerFactory.getLogger(CopyFile1stErrorRunner.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();

        // Register codec for custom message
        var deployName = CopyFileVerticle.class.getName();
        vertx.eventBus().getDelegate().registerDefaultCodec(CopyRequest.class, new CopyRequestCodec());

        // Deploy and send request to copy
        vertx.rxDeployVerticle(deployName)
                .flatMap(id -> {
                    logger.info("Copying 'ERROR.txt' to 'text1Copy.txt'");
                    return vertx.eventBus().<String>rxRequest(CopyFileVerticle.EB_ADDRESS, new CopyRequest("ERROR.txt", "text1Copy.txt"));
                })
                .flatMap(id -> {
                    logger.info("Copying 'text1.txt' to 'text2Copy.txt'");
                    return vertx.eventBus().<String>rxRequest(CopyFileVerticle.EB_ADDRESS, new CopyRequest("text1.txt", "text2Copy.txt"));
                })
                .doFinally(vertx::close)
                .subscribe(response -> logger.info("Result is {}", response.body()),
                        error -> logger.error("Failed with error {}", error.getMessage()));
        logger.info("<==main");
    }
}
