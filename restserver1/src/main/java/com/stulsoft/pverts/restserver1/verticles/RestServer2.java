/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pverts.restserver1.verticles;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class RestServer2  extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(RestServer2.class);
    public static final int MB = 1048576;

    @Override
    public void start() throws Exception {
        super.start();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(50 * MB));

        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");

        vertx.createHttpServer()
                .requestHandler(createRouter())
                .listen(port);
        logger.info("Server is listen now on {}", port);
    }

    private Router createRouter() {
        logger.info("Creating router");
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create().setBodyLimit(50 * MB));

        router
                .post("/restapi/accounts")
                .handler(this::handleRestApiDummy);

        return router;
    }

    private void handleRestApiDummy(final RoutingContext routingContext){
        vertx.setTimer(10000, l -> {
            JsonObject requestData = routingContext.getBodyAsJson();
            if (routingContext.response().closed()) {
                logger.error("REST API: Response is closed");
                System.out.println("REST API: Response is closed");
            } else {
                routingContext
                        .response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                        .end(requestData.encode());
            }
        });
    }
}
