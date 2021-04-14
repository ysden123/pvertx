/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.httpserver.cluster.verticle;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.reactivex.Completable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yuriy Stul
 */
public class Server extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @Override
    public Completable rxStart() {
        logger.info("Starting HTTP server...");

        Config conf = ConfigFactory.load();

        String host = conf.getString("host");
        int port = conf.getInt("port");

        Router router = Router.router(vertx);
        router.get("/service").handler(this::serviceHandler);

        return Completable.fromSingle(vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(port, host));
    }

    @Override
    public void stop(Promise<Void> stopFuture) {
        logger.info("Stopping HTTP server...");
        try {
            super.stop(stopFuture);
        } catch (Exception ex) {
            logger.error("Failed stopping server " + ex.getMessage(), ex);
        }
    }

    private void serviceHandler(RoutingContext routingContext) {
        logger.info("Handling service...");

        vertx.eventBus().request("serviceAddress", "Get page",
                messageAsyncResult -> commonServiceHandler(routingContext, messageAsyncResult));
    }

    private void commonServiceHandler(RoutingContext routingContext, AsyncResult<Message<Object>> ar) {
        var response = routingContext.response();
        if (ar.succeeded()) {
            handleMessage(response, ar.result());
        } else {
            handleErrorMessage(response, (ReplyException) ar.cause());
        }
    }

    private void handleMessage(HttpServerResponse response, Message<Object> message) {
        Map<String, String> results = new LinkedHashMap<>();
        results.put("response", message.body().toString());
        response
                .setStatusCode(200)
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(results));
    }

    private void handleErrorMessage(HttpServerResponse response, ReplyException exception) {
        int errCode = exception.failureCode();
        String errMsg = exception.getMessage();
        Map<String, String> results = new LinkedHashMap<>();
        results.put("error", String.format("Failed getting page. Error code=%d, error message: %s", errCode, errMsg));

        response
                .setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(Json.encodePrettily(results));
    }
}
