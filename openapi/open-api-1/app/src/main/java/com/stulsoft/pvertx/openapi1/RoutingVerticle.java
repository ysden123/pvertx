/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.openapi1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class RoutingVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(RoutingVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        RouterBuilder.create(vertx, "api.yaml")
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        logger.debug("Succeeded to build OpenAPI");
                        var routerBuilder = ar.result();
                        RouterBuilderOptions routerBuilderOptions = new RouterBuilderOptions()
                                .setOperationModelKey("operationModel");
                        routerBuilder.setOptions(routerBuilderOptions);
                        routerBuilder
                                .operation("test1")
                                .handler(routingContext -> {
                                    logger.debug("Processing test1 operation");
                                    var requestParameters = ((RequestParameters) routingContext
                                            .get(ValidationHandler.REQUEST_CONTEXT_KEY)).body().getJsonObject();
                                    var accId = requestParameters.getInteger("accId");
                                    var name = requestParameters.getString("name");
                                    logger.debug("accId={}, name={}", accId, name);
                                    var response = new JsonObject().put("accName", name + ":" + accId);
                                    routingContext.response().end(response.encodePrettily());
                                });

                        var router = routerBuilder.createRouter();
                        var server = vertx.createHttpServer(new HttpServerOptions()
                                .setPort(8080).setHost("localhost"));
                        server.requestHandler(router).listen();
                        logger.info("Listen on 8080 port");
                    } else {
                        var errorText = "Failed to build OpenAPI: " + ar.cause().getMessage();
                        logger.error(errorText);
                        startPromise.fail(errorText);
                    }
                });
        super.start(startPromise);
    }
}
