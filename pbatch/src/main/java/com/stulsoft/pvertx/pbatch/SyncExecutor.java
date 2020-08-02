/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pbatch;

import io.reactivex.Single;
import io.vertx.core.Vertx;

/**
 * @author Yuriy Stul
 */
public class SyncExecutor {
    private final Vertx vertx;

    public SyncExecutor(Vertx vertx) {
        this.vertx = vertx;
    }

    public Single<String> execute(String text){
        return Single.create(source ->{
            vertx.eventBus().request(
                    MessageProcessorVerticle.EB_ADDRESS,
                    text,
                    ar->{
                        if (!source.isDisposed()){
                            source.onSuccess(ar.result().toString());
                        }
                    });
        });
    }
}
