/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class CopyRequestCodec implements MessageCodec<CopyRequest, CopyRequest> {
    private static final Logger logger= LoggerFactory.getLogger(CopyRequestCodec.class);
    /**
     * Called by Vert.x when marshalling a message to the wire.
     *
     * @param buffer      the message should be written into this buffer
     * @param copyRequest the message that is being sent
     */
    @Override
    public void encodeToWire(Buffer buffer, CopyRequest copyRequest) {
//        logger.info("==>encodeToWire");
        // Encode object to string
        var json = copyRequest.toString();
        buffer.appendInt(json.length());
        buffer.appendString(json);
    }

    /**
     * Called by Vert.x when a message is decoded from the wire.
     *
     * @param pos    the position in the buffer where the message should be read from.
     * @param buffer the buffer to read the message from
     * @return the read message
     */
    @Override
    public CopyRequest decodeFromWire(int pos, Buffer buffer) {
//        logger.info("==>decodeFromWire");
        // My custom message starting from this *pos* of buffer
        var _pos = pos;

        // Length of JSON
        var length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        return new CopyRequest(jsonStr);
    }

    /**
     * If a message is sent <i>locally</i> across the event bus, this method is called to transform the message from
     * the sent type S to the received type R
     *
     * @param copyRequest the sent message
     * @return the transformed message
     */
    @Override
    public CopyRequest transform(CopyRequest copyRequest) {
//        logger.info("==>transform");
        // If a message is sent *locally* across the event bus.
        // This example sends message just as is
        return copyRequest;
    }

    /**
     * The codec name. Each codec must have a unique name. This is used to identify a codec when sending a message and
     * for unregistering codecs.
     *
     * @return the name
     */
    @Override
    public String name() {
//        logger.info("==>name");
        // Each codec must have a unique name.
        // This is used to identify a codec when sending a message and for unregistering codecs.
        return this.getClass().getSimpleName();
    }

    /**
     * Used to identify system codecs. Should always return -1 for a user codec.
     *
     * @return -1 for a user codec.
     */
    @Override
    public byte systemCodecID() {
//        logger.info("==>systemCodecID");
        // Always -1
        return -1;
    }
}
