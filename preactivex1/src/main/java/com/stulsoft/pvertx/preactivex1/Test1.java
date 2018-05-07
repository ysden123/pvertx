package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/7/2018
 */
public class Test1 {
    private static final Logger logger = LoggerFactory.getLogger(Test1.class);

    public static void hello(String... names) {
        logger.info("==>hello");
        Observable.fromArray(names).subscribe((s) -> {
            logger.info("Hello " + s + "!");
        });

        logger.info("<==hello");
    }

    public static void main(String[] args) {
        logger.info("==>main");
        hello("name 1", "name 2", "name 3");
        logger.info("<==main");
    }
}
