/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class SomeObjects2JsonArray {
    public static void main(String[] args){

        var jsonArray = new JsonArray();
        jsonArray.add(JsonObject.mapFrom(new SomeObject("name 1",111)));
        jsonArray.add(JsonObject.mapFrom(new SomeObject("name 2",222)));

        System.out.printf("jsonArray: %s%n", jsonArray);
    }
}
