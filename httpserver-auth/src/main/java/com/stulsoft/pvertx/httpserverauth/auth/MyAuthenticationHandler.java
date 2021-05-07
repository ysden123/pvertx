/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.httpserverauth.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;

/**
 * @author Yuriy Stul
 */
public class MyAuthenticationHandler implements AuthenticationHandler {
    @Override
    public void parseCredentials(RoutingContext routingContext, Handler<AsyncResult<Credentials>> handler) {

    }

    @Override
    public void handle(RoutingContext routingContext) {

    }

    @Override
    public Future<Credentials> parseCredentials(RoutingContext context) {
        return AuthenticationHandler.super.parseCredentials(context);
    }

    @Override
    public String authenticateHeader(RoutingContext context) {
        return AuthenticationHandler.super.authenticateHeader(context);
    }

    @Override
    public void postAuthentication(RoutingContext ctx) {
        AuthenticationHandler.super.postAuthentication(ctx);
    }
}
