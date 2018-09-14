/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.rxjava1.observer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example of Observer
 *
 * @author Yuriy Stul
 */
public class ObserverEx01 {
    private static Logger logger = LoggerFactory.getLogger(ObserverEx01.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Observable<String> observable = Observable.just("one", "two", "three");

        Observer<String> observer = new Observer<>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(String s) {
                logger.info("Data: {}", s);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onComplete() {
                logger.info("Done");
            }
        };

        observable.subscribe(observer);

        logger.info("<==main");
    }
}
