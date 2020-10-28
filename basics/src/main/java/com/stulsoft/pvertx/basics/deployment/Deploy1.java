/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.deployment;

import com.stulsoft.pvertx.common.Utils;

/**
 * @author Yuriy Stul
 */
public class Deploy1 {
    public static void main(String[] args) {
        var vertx = Utils.createVertx();

        vertx.deployVerticle(Vert1.class.getName(), dr1->{
            vertx.deployVerticle(Vert2.class.getName(), dr2->{
               vertx.deploymentIDs().forEach(id -> System.out.println("id: " + id));
               vertx.close();
            });
        });
    }
}
