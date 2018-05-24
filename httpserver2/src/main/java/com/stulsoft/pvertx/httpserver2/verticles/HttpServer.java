package com.stulsoft.pvertx.httpserver2.verticles;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Yuriy Stul
 * @since 5/2/2018
 */
public class HttpServer extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Starting HTTP server...");

        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");

        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/service").handler(this::serviceHandler);
        router.get("/service2").handler(this::service2Handler);

        server.requestHandler(router::accept).listen(port, ar -> {
            if (ar.succeeded()) {
                logger.info("HTTP server running on port {}", port);
                startFuture.complete();
            } else {
                logger.error("Failed start HTTP server. {}", ar.cause());
                startFuture.fail(ar.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        logger.info("Stopping HTTP server...");
        stopFuture.complete();
    }

    private void serviceHandler(RoutingContext routingContext) {
        logger.info("Handling service...");

        vertx.eventBus().send("serviceAddress", "Get page",
                messageAsyncResult -> commonServiceHandler(routingContext, messageAsyncResult));
    }

    private void service2Handler(RoutingContext routingContext) {
        logger.info("Handling service2...");

        vertx.eventBus().send("service2Address", "getService2", messageAsyncResult ->
                commonServiceHandler(routingContext, messageAsyncResult));
    }

    private void commonServiceHandler(RoutingContext routingContext, AsyncResult<Message<Object>> ar) {
        HttpServerResponse response = routingContext.response();
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
                .setStatusCode(400)
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
