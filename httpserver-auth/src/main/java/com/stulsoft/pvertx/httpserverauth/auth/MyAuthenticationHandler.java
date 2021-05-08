/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.httpserverauth.auth;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class MyAuthenticationHandler implements AuthenticationHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyAuthenticationHandler.class);

    private final AuthenticationProvider authenticationProvider;

    public MyAuthenticationHandler(final AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void parseCredentials(RoutingContext routingContext, Handler<AsyncResult<Credentials>> handler) {
        logger.info("==>parseCredentials");
    }

    @Override
    public void handle(RoutingContext routingContext) {
        logger.info("==>handle");

        var authorizationHeader = routingContext.request().getHeader("Authorization");
        logger.info("authorizationHeader: {}", authorizationHeader);
        if (authorizationHeader == null) {
            logger.error("Unauthorized 1");
            routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Unauthorized 1");
        } else {
            var tokens = authorizationHeader.split(" ");
            if (tokens.length != 2) {
                logger.error("Unauthorized 2");
                routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Unauthorized 2");
            } else {
                var token = tokens[1];
                authenticationProvider
                        .authenticate(new JsonObject()
                                .put("token", token))
                        .onComplete(authResult -> {
                            if (authResult.succeeded()) {
                                logger.info("Authorized");
                                logger.info("principal={}", authResult.result().principal().encodePrettily());
                                routingContext.setUser(User.fromName(authResult.result().principal().getString("username")));
                                routingContext.next();
                            } else {
                                logger.error("Unauthorized");
                                routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Unauthorized");
                            }
                        });
            }
        }
/*
        authenticationProvider
                .authenticate(new JsonObject()
                .put("username", "jwtuser")
                .put("password", "password"))
        .onComplete(authResult ->{
            if (authResult.succeeded()){
                logger.info("Authorized");
                routingContext.setUser(User.fromName("my_user_test"));
                routingContext.next();
            }else{
                logger.error("Unauthorized");
                routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Unauthorized");
            }
        }) ;
*/
    }

    @Override
    public Future<Credentials> parseCredentials(RoutingContext context) {
        logger.info("==>parseCredentials");
        return AuthenticationHandler.super.parseCredentials(context);
    }

    @Override
    public String authenticateHeader(RoutingContext context) {
        logger.info("==>authenticateHeader");
        return AuthenticationHandler.super.authenticateHeader(context);
    }

    @Override
    public void postAuthentication(RoutingContext ctx) {
        logger.info("==>postAuthentication");
        AuthenticationHandler.super.postAuthentication(ctx);
    }
}
