/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs Receiver verticle
 *
 * @author Yuriy Stul
 */
public class ReceiverRunner1 {
    private static final Logger logger = LoggerFactory.getLogger(ReceiverRunner1.class);

    public static void main(String[] args) {
        logger.info("==>main");
        ReceiverRunner.run();
    }
}
