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
public class AuthorizationProvider extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationProvider.class);

    public static final String EB_ADDRESS = AuthorizationProvider.class.getName();

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        logger.info("Initialize AuthorizationProvider");
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
        // todo YS Read from DB1
        var roles = new JsonArray()
                .add("admin");
        var permissions = new JsonArray()
                .add("add_account")
                .add("edit_account")
                .add("rerun_account")
                .add("global_go_live")
                .add("cancel_account")
                .add("*")
                .add("go_live")
                .add("remove_account")
                .add("show_merchant_data")
                .add("change_record_type");
        message.reply(new JsonObject()
        .put("roles", roles)
        .put("permissions", permissions));
    }
}
