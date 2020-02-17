package com.stulsoft.pvertx.pfuture;

import com.stulsoft.pvertx.common.Terminator;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
public class Main1 {
    private static Logger logger = LoggerFactory.getLogger(Main1.class);

    private static Future<String> foo() {
        Promise<String> promise = Promise.promise();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        promise.tryComplete("Good");
//        promise.tryFail("Bad");

        return promise.future();
    }

    public static void main(String[] args) {
        logger.info("Start");
        Vertx vertx = Vertx.vertx();
        logger.info("Call foo");
        Future<String> result = foo();

        result.setHandler(ar -> {
            if (ar.succeeded()) {
                logger.info("Result: {}", ar.result());
            } else {
                logger.error("Error: {}", ar.cause().getMessage());
            }
        });


        Terminator.terminate(vertx);
        logger.info("Finish");
    }
}
