/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pmongo.stream;

import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.stulsoft.pvertx.pmongo.util.Utils.createVertx;
import static com.stulsoft.pvertx.pmongo.util.Utils.mongoConfig;

/**
 * Stream like read and manipulation on stream
 *
 * @author Yuriy Stul
 */
public class StreamReaderEx1 {
    private static final Logger logger = LoggerFactory.getLogger(StreamReaderEx1.class);

    private Completable listObject(final MongoClient mongoClient) {
        return Completable.create(source -> {
                    var query = new JsonObject();
                    var findOptions = new FindOptions().setFields(new JsonObject().put("_id", 0));

                    var docs = mongoClient.findBatchWithOptions("test_01", query, findOptions).toFlowable();

                    docs.subscribe(
                            document -> logger.info("document: {}", document.encodePrettily()),
                            err -> {
                                logger.error(err.getMessage());
                                source.onError(err);
                            },
                            source::onComplete
                    );
                }
        );
    }

    private Completable countSum(final MongoClient mongoClient) {
        return Completable.create(source -> {
                    var query = new JsonObject();
                    var findOptions = new FindOptions().setFields(new JsonObject().put("_id", 0));

                    var docs = mongoClient.findBatchWithOptions("test_01", query, findOptions).toFlowable();

                    docs
                            .map(document -> document.getValue("sum", 0.0))
                            .map(value -> Double.parseDouble(value.toString()))
                            .reduce(Double::sum)
                            .subscribe(
                                    total -> {
                                        logger.info("Total sum = {}", total);
                                        source.onComplete();
                                    },
                                    err -> {
                                        logger.error(err.getMessage());
                                        source.onError(err);
                                    }
                            );
                }
        );
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.newInstance(createVertx());
        var mongoClient = MongoClient.createShared(vertx, mongoConfig());

        var inst = new StreamReaderEx1();
        var countDown = new CountDownLatch(2);

        inst.listObject(mongoClient)
                .subscribe(
                        countDown::countDown,
                        err -> countDown.countDown());

        inst.countSum(mongoClient)
                .subscribe(
                        countDown::countDown,
                        err -> countDown.countDown());

        try {
            countDown.await(5, TimeUnit.MINUTES);
        } catch (Exception ignore) {
        }

        mongoClient.close();
        vertx.close();
        logger.info("<==main");
    }
}
