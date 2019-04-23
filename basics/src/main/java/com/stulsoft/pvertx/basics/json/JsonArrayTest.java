/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class JsonArrayTest {
    private static void test1() {
        System.out.println("==>test1");
        var arr = new JsonArray();
        arr.add("text 1");
        arr.add("text 2");
        arr.add("text 3");
        System.out.println(arr.encodePrettily());

        var arr1 = new JsonArray(arr.encode());
        System.out.println(arr1.encodePrettily());

        System.out.println(arr1.getString(0));
        System.out.println("<==test1");
    }

    private static void test2() {
        System.out.println("==>test2");
        var arr = new JsonObject();
        arr.put("rows", new JsonArray());
        var rows = arr.getJsonArray("rows");
        rows.add("text 1");
        rows.add("text 2");
        rows.add("text 3");
        System.out.println(arr.encodePrettily());

        var arr1 = new JsonObject(arr.encode());
        System.out.println(arr1.encodePrettily());

        System.out.println(arr1.getJsonArray("rows").getString(0));
        System.out.println("<==test2");
    }

    public static void main(String[] args) {
        System.out.println("==>main");
        test1();
        test2();
        System.out.println("<==main");
    }
}
