/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.preactivex1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;

/**
 * @author Yuriy Stul
 */
public class Single2 {
	private static final Logger logger = LoggerFactory.getLogger(Single2.class);

	public static void main(String[] args) {
		logger.info("==>main");
		String[] result = { "" };
		Observable<String> observer = Observable.just("Hello"); // provides data
		observer.subscribe(s -> result[0] = s); // Callable as subscriber
		observer.doOnComplete(() -> logger.info("(1) result[0]:{}", result[0]));

		logger.info("(2) result[0]:{}", result[0]);
		observer.doOnDispose(() -> logger.info("==>onDispose"));

		logger.info("<==main");
	}
}
