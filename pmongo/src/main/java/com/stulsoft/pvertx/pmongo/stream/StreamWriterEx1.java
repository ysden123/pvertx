/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pmongo.stream;

import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.stulsoft.pvertx.pmongo.util.Utils.createVertx;
import static com.stulsoft.pvertx.pmongo.util.Utils.mongoConfig;

/**
 * @author Yuriy Stul
 */
public class StreamWriterEx1 {
    private static final Logger logger = LoggerFactory.getLogger(StreamWriterEx1.class);

    private Completable writeTest1(final MongoClient mongoClient) {
        return Completable.create(source -> {
            var operations = new ArrayList<BulkOperation>();
            for (int i = 1; i <= 10; ++i) {
                var newDocument = new JsonObject();
                newDocument.put("name", UUID.randomUUID().toString());
                newDocument.put("age", i);
                newDocument.put("sum", 1.0 * i);
                operations.add(BulkOperation.createInsert(newDocument));
            }
            mongoClient.bulkWrite("test_01", operations, ar -> {
                if (ar.succeeded()) {
                    logger.info("{} documents were added", ar.result().getInsertedCount());
                    source.onComplete();
                } else
                    source.onError(ar.cause());
            });
        });
    }

    private Completable writeTest2(final MongoClient mongoClient) {
        return Completable.create(source -> {
            var operations = new ArrayList<BulkOperation>();
            for (int i = 1; i <= 10; ++i) {
                var newDocument = new JsonObject();
                newDocument.put("name", UUID.randomUUID().toString());
                newDocument.put("age", i);
                newDocument.put("sum", 1.0 * i);
                operations.add(BulkOperation.createInsert(newDocument));
            }
            mongoClient.bulkWrite("test_01", operations, ar -> {
                if (ar.succeeded()) {
                    logger.info("{} documents were added", ar.result().getInsertedCount());
                    source.onComplete();
                } else
                    source.onError(ar.cause());
            });
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.newInstance(createVertx());
        var mongoClient = MongoClient.createShared(vertx, mongoConfig());

        var inst = new StreamWriterEx1();
        var countDown = new CountDownLatch(1);

        inst.writeTest1(mongoClient)
                .subscribe(
                        countDown::countDown,
                        err -> countDown.countDown()
                );

        try {
            countDown.await(5, TimeUnit.MINUTES);
        } catch (Exception ignore) {
        }

        mongoClient.close();
        vertx.close();
        logger.info("<==main");
    }
}
