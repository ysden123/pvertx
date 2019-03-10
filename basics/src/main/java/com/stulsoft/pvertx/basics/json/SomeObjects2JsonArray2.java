/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Yuriy Stul
 */
public class SomeObjects2JsonArray2 {
    public static void main(String[] args) {

        var list = List.of(new SomeObject("name 1", 111),
                new SomeObject("name 2", 222));

        var jsonArray = new JsonArray();
        for (SomeObject item : list) {
            jsonArray.add(JsonObject.mapFrom(item));
        }
        System.out.printf("jsonArray: %s%n", jsonArray);
    }
}
