/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class CopyRequest extends JsonObject {
    public CopyRequest(final String srcFileName, final String dstFileName) {
        super();
        put("srcFileName", srcFileName);
        put("dstFileName", dstFileName);
    }

    public String srcFileName() {
        return getString("srcFileName");
    }

    public String dstFileName() {
        return getString("dstFileName");
    }

}
