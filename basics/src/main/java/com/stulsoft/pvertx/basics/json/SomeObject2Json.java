/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.json;

import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class SomeObject2Json {
    public static void main(String[] args){

        var someObject = new SomeObject("name 1", 123);
        var jsonObject = JsonObject.mapFrom(someObject);

        System.out.printf("jsonObject: %s%n", jsonObject);
    }
}
