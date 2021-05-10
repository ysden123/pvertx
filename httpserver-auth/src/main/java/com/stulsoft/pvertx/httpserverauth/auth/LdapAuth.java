/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.httpserverauth.auth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class LdapAuth extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(LdapAuth.class);

    public static final String EB_ADDRESS = LdapAuth.class.getName();

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        logger.info("Initialize LDAP");
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer(EB_ADDRESS, this::handler);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void handler(Message<JsonObject> message) {
        // authenticate in LDAP

        // authorize in DB
        vertx.eventBus().<JsonObject>request(AuthorizationProvider.EB_ADDRESS, new JsonObject())
                .onComplete(authorizationResult -> {
                    if (authorizationResult.succeeded()) {
                        var properties = new JsonObject()
                                .put("name", "Yuriy")
                                .put("surname", "Stul")
                                .put("mail", "a@b.c")
                                .put("roles", authorizationResult.result().body().getJsonArray("roles"))
                                .put("permissions", authorizationResult.result().body().getJsonArray("permissions"));

                        var user = new JsonObject()
                                .put("username", "jwtuser")
                                .put("password", "")
                                .put("token", "28eb0b9b-812d-41c7-8986-8605a4365f70")
                                .put("properties", properties);

                        var response = new JsonObject()
                                .put("thumbnail", "fskfjsklfjskfjk")
                                .put("user", user);
                        message.reply(response);

                    } else {
                        logger.error(authorizationResult.cause().getMessage());
                        message.fail(401, authorizationResult.cause().getMessage());
                    }
                });


    }
}
