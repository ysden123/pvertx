package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/7/2018
 */
public class HelloWorld {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    public static void main(String[] args) {
        logger.info("==>HelloWorld.main");
        Flowable.just("Hello world").subscribe((s) -> {
            logger.info("s: {}", s);
        });
        logger.info("<==HelloWorld.main");
    }
}
