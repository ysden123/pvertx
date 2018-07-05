/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.deploy.options;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

/**
 * @author Yuriy Stul
 */
public class ConfigBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ConfigBuilder.class);

    private ConfigBuilder() {
    }

    static JsonObject load(final String path) {
        try (var stream = ConfigBuilder.class.getClassLoader().getResourceAsStream(path)) {
            return new JsonObject(new String(stream.readAllBytes()));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
}
