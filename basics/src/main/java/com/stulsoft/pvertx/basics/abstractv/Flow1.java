/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.abstractv;

import io.vertx.reactivex.core.Vertx;

/**
 * @author Yuriy Stul
 */
public class Flow1 extends AbstractFlow {
    @Override
    protected void foo() {
        System.out.println("In foo");
    }

    public static void main(String[] args) {
        System.out.println("==>main");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(new Flow1(), ar -> vertx.eventBus().request(AbstractFlow.EB_ADDRESS,"Test", ar2 ->{
            System.out.println(ar2.result().body().toString());
        }));
    }
}
