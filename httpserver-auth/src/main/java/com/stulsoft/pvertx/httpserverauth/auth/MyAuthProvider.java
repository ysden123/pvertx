/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.httpserverauth.auth;

import com.stulsoft.pvertx.httpserverauth.MainAuth;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.impl.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class MyAuthProvider implements AuthenticationProvider {
    private static Logger logger = LoggerFactory.getLogger(MyAuthProvider.class);
    @Override
    public void authenticate(JsonObject jsonObject, Handler<AsyncResult<User>> handler) {
        logger.debug("jsonObject: {}", jsonObject.encode());
//        handler.handle(Future.succeededFuture(new UserImpl()));
        handler.handle(Future.failedFuture("Invalid username/password"));
    }

    @Override
    public Future<User> authenticate(JsonObject credentials) {
        logger.debug("credentials: {}", credentials.encode());
        return AuthenticationProvider.super.authenticate(credentials);
    }

    @Override
    public void authenticate(Credentials credentials, Handler<AsyncResult<User>> resultHandler) {
        logger.debug("credentials: {}", credentials.toString());
        AuthenticationProvider.super.authenticate(credentials, resultHandler);
    }

    @Override
    public Future<User> authenticate(Credentials credentials) {
        logger.debug("credentials: {}", credentials.toString());
        return AuthenticationProvider.super.authenticate(credentials);
    }
}
