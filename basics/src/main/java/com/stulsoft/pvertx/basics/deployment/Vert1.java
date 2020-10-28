/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.deployment;

import io.vertx.core.AbstractVerticle;

/**
 * @author Yuriy Stul
 */
public class Vert1 extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("Vert1 started");
    }
}
