/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.pconfig;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 */
public class Test2 {
    private static Logger logger = LoggerFactory.getLogger(Test1.class);

    public static void main(String[] args) {
        logger.info("Started Test`");

        Vertx vertx = Vertx.vertx();

        ConfigManager.getInstance().load(vertx, ar -> {
            showConf();
            changeConfig();
            showConf();
        });

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();
        sc.close();

        vertx.close();
        logger.info("Finished Test`");
    }

    private static void showConf() {
        logger.info("Fields:");
        ConfigManager.config().fieldNames().forEach(logger::info);

        try {
            logger.info("ports:");
            JsonArray ports = ConfigManager.config().getJsonArray("ports");
            ports.forEach(o -> {
                JsonObject jo = (JsonObject) o;
                logger.debug("port={}", jo.getInteger("port"));

            });
            logger.debug(ports.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void changeConfig() {
        JsonArray ports = ConfigManager.config().getJsonArray("ports");
        ports.clear();
    }
}
