package com.stulsoft.pvertx.httpserver2.verticles;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

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
        router.get("/").handler(this::indexHandler);

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

    private void indexHandler(RoutingContext routingContext) {
        logger.info("Handling index...");

        HttpServerResponse response = routingContext.response();

        vertx.eventBus().send("serviceAddress", "Get page", messageAsyncResult -> {
            Map<String, String> results = new LinkedHashMap<>();
            if (messageAsyncResult.succeeded()) {
                results.put("response", messageAsyncResult.result().body().toString());
                response.putHeader("content-type", "application/json").end(Json.encodePrettily(results));
            } else {
                ReplyException ex = (ReplyException) messageAsyncResult.cause();
                int errCode = ex.failureCode();
                String errMsg = ex.getMessage();

                results.put("error", String.format("Failed getting page. Error code=%d, error message: %s", errCode, errMsg));

                response
                        .setStatusCode(400)
                        .putHeader("content-type", "application/json")
                        .end(Json.encodePrettily(results));
            }
        });
    }

}
