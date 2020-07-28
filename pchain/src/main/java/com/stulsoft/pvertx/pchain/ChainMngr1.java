/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pchain;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Yuriy Stul
 */
public class ChainMngr1 {
    private static final Logger logger = LoggerFactory.getLogger(ServiceVerticle.class);

    public static Completable manager1(int n, final Vertx vertx) {
        logger.info("==>manager1");

        return Completable.create(source -> {

            var requests = new CompletableSource[n];
            var msgs = Utils.generateMsg(n);
            for (var i = 0; i < n; ++i) {
                var theI = i;
                requests[i] = Completable.create(source2 ->
                        vertx.eventBus()
                                .request(ServiceVerticle.EB_ADDRESS, msgs[theI],
                                        ar -> source2.onComplete()));
            }

            Completable.concatArray(requests).subscribe(source::onComplete);
        });
    }

    public static Completable manager2(int n, final Vertx vertx) {
        logger.info("==>manager2");

        return Completable.create(source -> {

            var requests = new ArrayList<CompletableSource>();
            var msgs = Utils.generateMsg(n);
            for (var msg : msgs) {
                requests.add(Completable.create(source2 ->
                        vertx.eventBus()
                                .request(ServiceVerticle.EB_ADDRESS, msg,
                                        ar -> source2.onComplete())));
            }

            CompletableSource[] requestArray = new CompletableSource[requests.size()];
            requestArray = requests.toArray(requestArray);

            Completable.concatArray(requestArray).subscribe(source::onComplete);
        });
    }
}
