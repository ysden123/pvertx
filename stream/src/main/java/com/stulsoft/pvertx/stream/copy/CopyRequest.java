/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.stream.copy;

import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author Yuriy Stul
 */
public class CopyRequest {
    private String srcFileName;
    private String dstFileName;

    /**
     * Initializes a new instance of the CopyRequest class from a JSon string
     *
     * @param json the JSon string
     */
    public CopyRequest(final String json) {
        var jsonObject = new JsonObject(json);
        srcFileName = jsonObject.getString("srcFileName");
        dstFileName = jsonObject.getString("dstFileName");
    }

    /**
     * Initializes of the CopyRequest class
     *
     * @param srcFileName the source file name
     * @param dstFileName the destination file name`
     */
    public CopyRequest(final String srcFileName, final String dstFileName) {
        this.srcFileName = srcFileName;
        this.dstFileName = dstFileName;
    }

    public String srcFileName() {
        return srcFileName;
    }

    public String dstFileName() {
        return dstFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CopyRequest)) return false;
        CopyRequest that = (CopyRequest) o;
        return Objects.equals(srcFileName, that.srcFileName) &&
                Objects.equals(dstFileName, that.dstFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcFileName, dstFileName);
    }

    @Override
    public String toString() {
        return "CopyRequest{" +
                "srcFileName='" + srcFileName + '\'' +
                ", dstFileName='" + dstFileName + '\'' +
                '}';
    }
}
