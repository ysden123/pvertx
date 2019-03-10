package com.stulsoft.pvertx.pfuture;

import com.stulsoft.pvertx.common.Terminator;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private static Future<String> foo() {
        Future<String> future = Future.future();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        future.tryComplete("Good");
        future.tryFail("Bad");

        return future;

    }

    public static void main(String[] args) {
        logger.info("Start");
        Vertx vertx = Vertx.vertx();
        logger.info("Call foo");
        Future<String> result = foo();
        boolean status = result.succeeded();

        logger.info("status={}", status);
        if (status)
            logger.info("Result: {}", result.result());
        else {
            logger.error("Error: {}", result.cause().getMessage());
        }


        Terminator.terminate(vertx);
        logger.info("Finish");
    }
}
