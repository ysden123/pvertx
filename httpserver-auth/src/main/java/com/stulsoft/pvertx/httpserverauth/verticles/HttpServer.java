package com.stulsoft.pvertx.httpserverauth.verticles;

import com.stulsoft.pvertx.httpserverauth.auth.MyAuthProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
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
    public void start(Promise<Void> startPromise){
        logger.info("Starting HTTP server...");

        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");

        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        AuthenticationProvider authProvider = new MyAuthProvider();
        AuthenticationHandler authenticationHandler = BasicAuthHandler.create(authProvider);
        router.route("/*").handler(authenticationHandler);
        router.get("/").handler(this::indexHandler);

        server.requestHandler(router).listen(port, ar -> {
            if (ar.succeeded()) {
                logger.info("HTTP server running on port {}", port);
                startPromise.complete();
            } else {
                logger.error("Failed start HTTP server. " + ar.cause().getMessage(), ar.cause());
                startPromise.fail(ar.cause());
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise){
        logger.info("Stopping HTTP server...");
        stopPromise.complete();
    }

    private void indexHandler(RoutingContext routingContext) {
        logger.info("Handling index...");
        Map<String,String> results = new LinkedHashMap<>();
        results.put("response", "Hello");
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(Json.encodePrettily(results));
    }

}
