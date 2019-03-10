/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.common;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author Yuriy Stul
 */
public class ConfigRetrieverFactory {
    private ConfigRetrieverFactory(){}

    public static ConfigRetriever configRetriever(final Vertx vertx, final String path){
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", path));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        return retriever;
    }
}
