/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex2.fututure;

import io.vertx.rxjava.core.CompositeFuture;
import io.vertx.rxjava.core.Future;
import io.vertx.rxjava.core.Promise;
import io.vertx.rxjava.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * All succeeded
 *
 * @author Yuriy Stul
 */
public class CompF1 {
    private final static Logger logger = LoggerFactory.getLogger(CompF1.class);
    private final Vertx vertx = Vertx.vertx();

    public static void main(String[] args) {
        logger.info("==>main");
        new CompF1().runtTest();
        logger.info("<==main");
    }

    private Future<String> f1() {
        logger.info("==>f1");
        var p = Promise.<String>promise();
        vertx.setTimer(1000, l -> p.complete("done 1"));
        logger.info("<==f1");
        return p.future();
    }

    private Future<String> f2() {
        logger.info("==>f2");
        var p = Promise.<String>promise();
        vertx.setTimer(1100, l -> p.complete("done 2"));
        logger.info("<==f2");
        return p.future();
    }

    private void runtTest() {
        logger.info("==>runtTest");
        var fList = new ArrayList<Future>();
        fList.add(f1());
        fList.add(f2());
        var result = CompositeFuture.all(fList);
        result.onComplete(r -> {
            logger.info("r.succeeded(): {}", r.succeeded());
            vertx.close();
        });
        logger.info("<==runtTest");
    }
}
