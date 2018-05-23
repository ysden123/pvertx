package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/8/2018
 */
public class Single1 {
    private static final Logger logger = LoggerFactory.getLogger(Single1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        String[] result = {""};
        Disposable single = Observable.just("Hello1", "Hello2", "Hello3")
                .subscribe(
                        s -> {
                            logger.info("s={}", s);
                            result[0] += " " + s;
                        },
                        e -> {
                            throw new RuntimeException(e.getMessage());
                        },
                        () -> {
                            logger.info("result: {}", result[0]);
                        });
        single.dispose();
        logger.info("<==main");
    }
}
