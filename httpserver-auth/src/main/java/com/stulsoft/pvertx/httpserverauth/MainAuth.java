package com.stulsoft.pvertx.httpserverauth;

import com.stulsoft.pvertx.common.Terminator;
import com.stulsoft.pvertx.httpserverauth.verticles.HttpServer;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 * @since 5/2/2018
 */
public class MainAuth {
    private static Logger logger = LoggerFactory.getLogger(MainAuth.class);

    public static void main(String[] args) {
        logger.info("Started MainAuth");
        Vertx vertx = Vertx.vertx();

        Verticle httpServer = new HttpServer();

        logger.info("Deploying HttpServer");
        vertx.deployVerticle(httpServer);

        Terminator.terminate(vertx);
        logger.info("Stopped MainAuth");
    }
}
