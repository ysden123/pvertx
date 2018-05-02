package com.stulsoft.pvertx.httpserver1.verticles;

import com.typesafe.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.typesafe.config.ConfigFactory;

/**
 * @author Yuriy Stul
 * @since 5/2/2018
 */
public class HttpServer extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
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
    public void stop(Future<Void> stopFuture) throws Exception {
        logger.info("Stopping HTTP server...");
        stopFuture.complete();
    }

    private void indexHandler(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/html").end("Hello");
    }

}
