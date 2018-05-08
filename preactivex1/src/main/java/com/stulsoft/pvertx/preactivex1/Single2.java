package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/8/2018
 */
public class Single2 {
    private static final Logger logger = LoggerFactory.getLogger(Single2.class);

    public static void main(String[] args) {
        logger.info("==>main");
        String[] result = {""};
        Observable<String> observer = Observable.just("Hello"); // provides datea
        observer.subscribe(s -> result[0] = s); // Callable as subscriber
        observer.doOnComplete(() -> logger.info("(1) result[0]:{}", result[0]));

        logger.info("(2) result[0]:{}", result[0]);
        observer.doOnDispose(()->logger.info("==>onDispose"));

        logger.info("<==main");
    }
}
