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
import java.util.stream.Collectors;

/**
 * One failed
 *
 * @author Yuriy Stul
 */
public class CompF2 {
    private final static Logger logger = LoggerFactory.getLogger(CompF2.class);
    private final Vertx vertx = Vertx.vertx();

    public static void main(String[] args) {
        logger.info("==>main");
        new CompF2().runtTest();
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
        vertx.setTimer(1100, l -> p.fail("error 2"));
        logger.info("<==f2");
        return p.future();
    }

    private Future<String> f3() {
        logger.info("==>f3");
        var p = Promise.<String>promise();
        vertx.setTimer(1000, l -> p.complete("done 3"));
        logger.info("<==f3");
        return p.future();
    }

    private Future<String> f4() {
        logger.info("==>f4");
        var p = Promise.<String>promise();
        vertx.setTimer(1100, l -> p.fail("error 4"));
        logger.info("<==f4");
        return p.future();
    }

    private void runtTest() {
        logger.info("==>runtTest");
        var fList = new ArrayList<Future>();
        fList.add(f1());
        fList.add(f2());
        fList.add(f3());
        fList.add(f4());
        var result = CompositeFuture.all(fList);
        result.setHandler(r -> {
            logger.info("r.succeeded(): {}", r.succeeded());
            if (r.failed()) {
                var joinedError = String.join("; ",
                        fList
                                .stream()
                                .filter(Future::failed)
                                .map(f -> f.cause().getMessage())
                                .collect(Collectors.toList()));
                logger.error("Error: {}", joinedError);
            }

            var joinedSuccess = String.join(", ",
                    fList
                            .stream()
                            .filter(Future::succeeded)
                            .map(f -> f.result().toString())
                            .collect(Collectors.toList()));
            logger.info("Success: {}", joinedSuccess);
            vertx.close();
        });
        logger.info("<==runtTest");
    }
}
