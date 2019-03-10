/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.common;

import io.vertx.core.Vertx;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 */
public class Terminator {
    private Terminator() {
    }

    public static void terminate(final Vertx vertx){
        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();
        sc.close();

        vertx.close();
    }
}
