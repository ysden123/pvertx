package com.stulsoft.pvertx.pconfig;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
class ConfigManager {
    private static Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager instance = null;
    private final static Object syncObject = new Object();
    private JsonObject config;

    private ConfigManager() {

    }

    static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (syncObject) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    void load(Vertx vertx, Handler<AsyncResult> handler) {
        logger.info("loading config...");
        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", "conf/config.json"));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(fileStore);
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.getConfig(ar -> {
            if (ar.failed()) {
                logger.error("Failed getting configuration. {}", ar.cause().getMessage());
            } else {
                config = ar.result();
            }
            handler.handle(ar);
            logger.info("loaded config");
        });
    }

    static JsonObject config() {
        return getInstance().config.copy();

    }
}
