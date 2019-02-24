/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pverts.restserver1.verticles;

import com.stulsoft.pverts.restserver1.data.ServiceStatus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.vertx.core.Future;
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
    private static Logger logger = LoggerFactory.getLogger(RestServer.class);
    private Disposable server;
    private Disposable someDisposable;
    private ExecutorService es = Executors.newFixedThreadPool(3);

    /**
     * Start the verticle.<p>
     * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.<p>
     * If your verticle does things in its startup which take some time then you can override this method
     * and call the startFuture some time later when start up is complete.
     *
     * @param startFuture a future which should be called when verticle start-up is complete.
     */
    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Starting RestServer...");

        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");

        server = vertx.createHttpServer()
                .requestHandler(createRouter()::accept)
                .rxListen(port)
                .toObservable()
                .subscribe(httpServer -> {
                    logger.info("Started server on {}", port);
                    startFuture.complete();
                }, t -> startFuture.fail(t.getMessage()));

    }

    /**
     * Stop the verticle.<p>
     * This is called by Vert.x when the verticle instance is un-deployed. Don't call it yourself.<p>
     * If your verticle does things in its shut-down which take some time then you can override this method
     * and call the stopFuture some time later when clean-up is complete.
     *
     * @param stopFuture a future which should be called when verticle clean-up is complete.
     */
    @Override
    public void stop(Future<Void> stopFuture) {
        logger.info("Stopping RestServer...");
        someDisposable.dispose();
        server.dispose();
        stopFuture.complete();
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
