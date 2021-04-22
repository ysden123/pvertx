/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.abstractv;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
/**
 * @author Yuriy Stul
 */
public class AbstractVerticleEx1 extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("Started");
    }

    public static void main(String[] args) {
        System.out.println("==>main");
        var vertx = Vertx.vertx();

        vertx.deployVerticle(new AbstractVerticleEx1());
    }
}
