/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.abstractv;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;

/**
 * @author Yuriy Stul
 */
public abstract class AbstractFlow extends AbstractVerticle {
    public static final String EB_ADDRESS = AbstractFlow.class.getName();

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
        System.out.println("Started");
    }

    protected abstract void foo();

    private void handler(Message<String> message){
        foo();
        message.reply("Done");
    }
}
