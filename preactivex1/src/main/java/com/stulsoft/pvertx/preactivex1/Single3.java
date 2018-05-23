package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/8/2018
 */
public class Single3 {
    private static Logger logger = LoggerFactory.getLogger(Single3.class);

    private static Single<String> generateData() {
        return Single.just("data 1");
    }

    private static Single<String> generateDataWithError() {
        return Single.error(new RuntimeException("test error"));
    }

    private static void runner(Single<String> data){
        data.subscribe(
                s -> {
                    logger.info("onSuccess");
                    logger.info("s={}", s);
                },
                e -> {
                    logger.info("onError");
                    logger.info("e={}", e.getMessage());
                }
        )
        .dispose();
    }

    public static void main(String[] args) {
        logger.info("==>main");
        logger.info("generateData()");
        runner(generateData());

        logger.info("generateDataWithError()");
        runner(generateDataWithError());

        logger.info("<==main");
    }
}
