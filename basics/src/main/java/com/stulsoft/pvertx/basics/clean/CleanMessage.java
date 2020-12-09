/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.clean;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.eventbus.Message;

/**
 * @author Yuriy Stul
 */
public class CleanMessage extends Message<JsonObject> {
    private final JsonObject json;

    CleanMessage(JsonObject json) {
        super(null);
        this.json = json;
    }

    public void setParam1(JsonObject param1) {
        json.put("param1", param1);
    }

    public JsonObject getParam1() {
        return json.getJsonObject("param1");
    }

    @Override
    public JsonObject body() {
        return json;
    }
}
