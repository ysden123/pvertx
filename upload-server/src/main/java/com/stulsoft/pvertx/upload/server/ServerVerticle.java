/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.upload.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Uploads a file and saved it into temporary folder.
 *
 * @author Yuriy Stul
 */
public class ServerVerticle extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(ServerVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Starting HTTP server...");
        // todo

        Config conf = ConfigFactory.load();

        int port = conf.getInt("port");
        var server = vertx.createHttpServer();

        var router = Router.router(vertx);
        router.post("/upload").handler(this::uploadHandler);

        server
                .requestHandler(router::accept)
                .listen(port, listenResult -> {
                    if (listenResult.succeeded()) {
                        logger.info("Upload server is running on port {}", port);
                        startFuture.complete();
                    } else {
                        logger.error("Failed start upload server. {}", listenResult.cause().getMessage());
                        startFuture.fail(listenResult.cause());
                    }
                });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        logger.info("Stopping HTTP server...");
        stopFuture.complete();
    }

    private void uploadHandler(final RoutingContext routingContext) {
        logger.info("==>uploadHandler");
        var request = routingContext.request();

        showInfo(request);

        request.setExpectMultipart(true);

        request.uploadHandler(upload -> {
            logger.debug("==>upload");
            upload.exceptionHandler(cause -> {
                logger.debug("==>exceptionHandler");
                logger.error(cause.getMessage(), cause);
                request.response()
                        .setChunked(true)
                        .end("Upload failed: " + cause.getMessage());
            });

            upload.endHandler(v -> {
                logger.debug("==>endHandler");
                request.response()
                        .setChunked(true)
                        .end("Successfully uploaded " + upload.filename());
            });


            try {
                var tmpFileName = File.createTempFile("uploadTest", ".tmp").getAbsolutePath();
                logger.debug("Saving into {}", tmpFileName);
                upload.streamToFileSystem(tmpFileName);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        });
    }

    private void showInfo(final HttpServerRequest request) {
        logger.debug("request: {}", request);
        request
                .headers()
                .names()
                .forEach(headerName -> logger.debug("Header {} : {}", headerName, request.headers().get(headerName)));
    }
}
