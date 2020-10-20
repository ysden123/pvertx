/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.chain.flatmap;

import com.stulsoft.pvertx.common.Utils;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class ChainWithFlatMapEx2 {
    private static final Logger logger = LoggerFactory.getLogger(ChainWithFlatMapEx2.class);
    private final Vertx vertx;

    public ChainWithFlatMapEx2(Vertx vertx) {
        this.vertx = vertx;
    }

    void test1() {
        logger.info("==>test1");
        foo();
    }

    void test2() {
        logger.info("==>test2");
        foo().subscribe();
    }

    private Single<String> foo() {
        logger.info("==>foo");
        Single<String> result;

        result = f1()
                .flatMap(r1 -> f2());
        logger.info("<==foo");
        return result;
    }


/*
    private Single<String> foo() {
        logger.info("==>foo");
        return Single.create(source -> {
            f1()
                    .flatMap(r1 -> f2())
                    .subscribe(r -> source.onSuccess("All done"));
        });
    }
*/

    private Single<String> f1() {
        return Single.create(source -> {
            vertx.setTimer(1000, l -> {
                logger.info("Finish in f1");
                source.onSuccess("Done in f1");
            });
        });
    }

    private Single<String> f2() {
        return Single.create(source -> {
            vertx.setTimer(1000, l -> {
                logger.info("Finish in f2");
                source.onSuccess("Done in f2");
            });
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var vertx = Vertx.newInstance(Utils.createVertx());
        var cl = new ChainWithFlatMapEx2(vertx);
//        cl.test1();
        cl.test2();

        vertx.setTimer(5000, l -> vertx.close());
    }
}
