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

    public static void terminate(final io.vertx.core.Vertx vertx){
        System.out.println("For end enter any line or empty line");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();

        vertx.close();
    }

    public static void terminate(final io.vertx.reactivex.core.Vertx vertx){
        System.out.println("For end enter any line or empty line");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();

        vertx.close();
    }
}
