/*
 * Created by Yuriy Stul 24 May 2018
 */
package com.stulsoft.pvertx.preactivex2;

import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

/**
 * @author Yuriy Stul
 */
public class Single1 {
    private static final Logger logger = LoggerFactory.getLogger(Single1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle("com.stulsoft.pvertx.preactivex2.Service");
        Single<Message<String>> handler = vertx.eventBus().rxRequest("address", "The message");
        handler.subscribe(r -> logger.info("Response: {}", r.body()),
                error -> logger.error("Failure (1). {}", error.getMessage()));
		/*
		vertx.setTimer(5000, l -> {
			vertx.close();
			logger.info("<==main");
		});
		*/
        /**/
        vertx.close();
        logger.info("<==main");
        /**/
    }

}
