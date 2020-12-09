/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.clean;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class CleanMessageCodec  implements MessageCodec<CleanMessage,CleanMessage> {
    @Override
    public void encodeToWire(Buffer buffer, CleanMessage cleanMessage) {
        var json = cleanMessage.toString();
        buffer.appendInt(json.length());
        buffer.appendString(json);
    }

    @Override
    public CleanMessage decodeFromWire(int i, Buffer buffer) {
        var _pos = i;

        // Length of JSON
        var length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        return new CleanMessage(new JsonObject(jsonStr));
    }

    @Override
    public CleanMessage transform(CleanMessage cleanMessage) {
        return cleanMessage;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
