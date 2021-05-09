/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.httpserverauth.verticles;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yuriy Stul
 */
public class HttpServer2
        extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer2.class);
    JWTAuth jwtProvider;
    JWTOptions jwtOptions;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        jwtOptions = new JWTOptions()
                .setExpiresInMinutes(100)
                .setPermissions(Arrays.asList("permission1", "permission2", "permission3"));
        try {
            jwtProvider = JWTAuth.create(vertx, new JWTAuthOptions()
                    .addPubSecKey(new PubSecKeyOptions()
                            .setAlgorithm("HS256")
                            .setBuffer("keyboard cat")));

        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }

    }

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting HTTP server...");
        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");

        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        router.get("/login").handler(this::login);
        var authenticationHandler = JWTAuthHandler.create(jwtProvider);
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
    public void stop(Promise<Void> stopPromise) {
        logger.info("Stopping HTTP server...");
        stopPromise.complete();
    }

    private void login(RoutingContext routingContext) {
        logger.info("==>login");
        String token = jwtProvider.generateToken(new JsonObject().put("username", "jwtuser"), jwtOptions);
        jwtProvider
                .authenticate(new JsonObject());
        logger.info("token = {}", token);
        var credentials = new TokenCredentials(token);
        logger.info("credentials: {}", credentials.toString());
        var responseBody = new JsonObject()
                .put("token", token);

        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(responseBody.encodePrettily());
    }

    private void indexHandler(RoutingContext routingContext) {
        logger.info("Handling index...");
        var user = routingContext.user();
        if (user != null) {
            var username = user.principal().getString("username");
            logger.info("User: {}", username);
        } else {
            logger.error("user is undefined");
        }
        Map<String, String> results = new LinkedHashMap<>();
        results.put("response", "Hello");
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json").end(Json.encodePrettily(results));
    }
}
