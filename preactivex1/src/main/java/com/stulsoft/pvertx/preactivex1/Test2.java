package com.stulsoft.pvertx.preactivex1;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/7/2018
 */
public class Test2 {
    private static final Logger logger = LoggerFactory.getLogger(Test2.class);

    private static Observable<String> work() {
        logger.info("==>work");
        return Observable.create(emmiter -> {
            emmiter.onNext("test1");
            logger.info("after onNext");
            emmiter.onComplete();
            logger.info("after onComplete");
        });
    }

    public static void main(String[] args) {
        logger.info("==>main");
        Disposable d = work().subscribe(s -> logger.info("Received {}", s));
        d.dispose();
        logger.info("<==main");
    }
}
