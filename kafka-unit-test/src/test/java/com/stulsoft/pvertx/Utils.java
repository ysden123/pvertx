/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx;

import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class Utils {
    private Utils(){}

    public static JsonObject jsonFromResource(final String path){
        var s=Utils.class.getClassLoader().getResourceAsStream(path);
        try {
            return new JsonObject(new String(s.readAllBytes()));
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
