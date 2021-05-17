/*
 * Copyright (c) 2021. StulSoft
 */

package com.stulsoft.pvertx.basics.timer;

import com.stulsoft.pvertx.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Stul
 */
public class TimerEx01 {
    private static final Logger logger = LoggerFactory.getLogger(TimerEx01.class);

    public static void main(String[] args) {
        logger.info("==>main");

        var vertx = Utils.createVertx();

        logger.info("Started");
        vertx.setTimer(1000, __ -> {
            logger.info("Timer");
        });

        vertx.setPeriodic(2000, __ ->{
            logger.info("Periodic");
        });

        vertx.setTimer(10000, __ -> vertx.close());
    }
}
