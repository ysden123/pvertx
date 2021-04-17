/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pverts.restserver1.verticles;

import com.stulsoft.pverts.restserver1.data.ServiceStatus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yuriy Stul
 */
public class RestServer extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(RestServer.class);
    private Disposable server;
    private Disposable someDisposable;
    private final ExecutorService es = Executors.newFixedThreadPool(3);

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting RestServer...");

        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");

        server = vertx.createHttpServer()
                .requestHandler(createRouter())
                .rxListen(port)
                .toObservable()
                .subscribe(httpServer -> {
                    logger.info("Started server on {}", port);
                    startPromise.complete();
                }, t -> startPromise.fail(t.getMessage()));

    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        logger.info("Stopping RestServer...");
        someDisposable.dispose();
        server.dispose();
        stopPromise.complete();
    }

    private Router createRouter() {
        logger.info("Creating router");
        Router router = Router.router(vertx);

        router.get("/")
                .handler(routingContext -> routingContext
                        .response()
                        .sendFile("index.html"));

        router.get("/v1/status/all")
                .handler(routingContext -> {
                    logger.info("/v1/status/all");
                    someDisposable = all()
                            .toObservable()
                            .subscribe(response -> sendJson(response, routingContext));
                });

        router.get("/v1/status/service/:name").
                handler(routingContext -> {
                    var name = routingContext.request().getParam("name");
                    logger.info("/v1/status/service/{}", name);
                    someDisposable = serviceByName(name)
                            .toObservable()
                            .subscribe(response -> sendJson(response, routingContext),
                                    error -> sendError(error, routingContext));
                });

        router.get("/v1/status/service/:name/id/:id").
                handler(routingContext -> {
                    var name = routingContext.request().getParam("name");
                    var id = routingContext.request().getParam("id");
                    logger.info("/v1/status/service/{}/id/{}", name, id);
                    someDisposable = serviceByNameId(name, id)
                            .toObservable()
                            .subscribe(response -> sendJson(response, routingContext),
                                    error -> sendError(error, routingContext));
                });
//        curl -X POST http://localhost:8080/rest/long
        router.post("/rest/long")
                .handler(routingContext ->
                        vertx.executeBlocking(blockingHandler -> {
                                    logger.debug("in blockingHandler");
                                    var deliveryOptions = new DeliveryOptions().setSendTimeout(120_000);
                                    vertx.eventBus().request(
                                            LongService.EB_ADDRESS,
                                            "test",
                                            deliveryOptions,
                                            ar -> {
                                                if (ar.succeeded()) {
                                                    blockingHandler.complete(ar.result().body());
                                                } else {
                                                    blockingHandler.fail(ar.cause().getMessage());
                                                }
                                            });
                                },
                                resultHandler -> {
                                    logger.debug("in resultHandler");
                                    if (resultHandler.succeeded()) {
                                        if (routingContext.response().closed()) {
                                            logger.error("Response was closed");
                                        } else
                                            routingContext.response().end(resultHandler.result().toString());
                                    } else {
                                        logger.error(resultHandler.cause().getMessage());
                                        routingContext.response().setStatusCode(500).end(resultHandler.cause().getMessage());
                                    }
                                }
                        )
                );

        return router;
    }

    private Single<JsonArray> all() {
        var result = new JsonArray()
                .add(JsonObject.mapFrom(new ServiceStatus("service_1", "1", "OK")))
                .add(JsonObject.mapFrom(new ServiceStatus("service_2", "2", "ERROR")))
                .add(JsonObject.mapFrom(new ServiceStatus("service_3", "3", "OK")));
        return Single.fromFuture(es.submit(() -> result));
    }

    private Single<JsonObject> serviceByName(String name) {
        if (name.equals("error")) {
            return Single.error(new RuntimeException("Not existing service " + name));
        } else {
            return Single.fromFuture(es.submit(() -> JsonObject.mapFrom(new ServiceStatus("name", "1", "OK"))));
        }
    }

    private Single<JsonObject> serviceByNameId(String name, String id) {
        if (name.equals("error")) {
            return Single.error(new RuntimeException("Not existing service " + name));
        } else if (id.equals("error")) {
            return Single.error(new RuntimeException("Not existing id " + id));
        } else {
            return Single.fromFuture(es.submit(() -> JsonObject.mapFrom(new ServiceStatus(name, id, "OK"))));
        }
    }

    private void sendJson(JsonObject json, RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(json.toString());
    }

    private void sendJson(JsonArray json, RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(json.toString());
    }

    private void sendError(Throwable error, RoutingContext routingContext) {
        routingContext
                .response()
                .setStatusCode(201)
                .end(error.getMessage());
    }
}
