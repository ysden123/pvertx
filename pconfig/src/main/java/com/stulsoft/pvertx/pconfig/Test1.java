package com.stulsoft.pvertx.pconfig;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author Yuriy Stul
 * @since 5/3/2018
 */
public class Test1 {
    private static Logger logger = LoggerFactory.getLogger(Test1.class);

    public static void main(String[] args) {
        logger.info("Started Test1");
        Vertx vertx = Vertx.vertx();

        Verticle v1 = new Verticle1();


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
                JsonObject config = ar.result();
                config.fieldNames().forEach(s -> logger.info("field name {}, value: {}", s, config.getString(s)));
            }
        });

        vertx.deployVerticle(v1);

        System.out.println("For end enter any line");
        Scanner sc = new Scanner(System.in);
        sc.next();

        vertx.close();
        logger.info("Finished Test1");
    }
}
