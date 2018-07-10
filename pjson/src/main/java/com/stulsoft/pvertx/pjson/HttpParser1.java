/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pjson;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.parsetools.JsonEventType;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.parsetools.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yuriy Stul
 */
public class HttpParser1 {
    private static final Logger logger = LoggerFactory.getLogger(HttpParser1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.vertx();
        var client = vertx.createHttpClient();
        // http://httpbin.org/stream/10
        var req = client.get(new RequestOptions()
                .setHost("httpbin.org")
                .setPort(80)
                .setSsl(false)
                .setURI("/stream/100"));    // max - 100
        req.putHeader("Content-type", "application/json");

        req.handler(reqRes -> {
            if (reqRes.statusCode() == HttpResponseStatus.OK.code()) {
                var counter = new AtomicInteger();
                var parser = JsonParser.newParser(reqRes.toObservable());
                parser.objectValueMode()
                        .exceptionHandler(t -> {
                            logger.error(t.getMessage(), t);
                            vertx.close();
                        })
                        .endHandler(v -> {
                            logger.info("Processed {} records", counter.get());
                            vertx.close();
                        })
                        .handler(event -> {
                            if (event.type() == JsonEventType.VALUE) {
                                if (counter.incrementAndGet() <= 10) {  // output first 10 records
                                    logger.info("{}->{}", event.fieldName(), event.value().toString());
                                    var fieldNames=event.objectValue().fieldNames();
                                    logger.info("\tFields: {}", fieldNames);
                                }
                            } else {
                                logger.info("{}", event.type());
                            }
                        });
            } else {
                logger.error("Failed {} {}", reqRes.statusCode(), reqRes.statusMessage());
            }
            vertx.close();
            logger.info("<==main");
        });
        req.exceptionHandler(t -> {
            logger.error(t.getMessage());
            vertx.close();
            logger.info("<==main");
        });
        req.end();
    }
}
