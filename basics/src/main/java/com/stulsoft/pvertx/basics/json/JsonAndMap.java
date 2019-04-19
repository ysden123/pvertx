/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.json;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Create <i>JsonObject</i> from maps
 *
 * @author Yuriy Stul
 */
public class JsonAndMap {
    private static final Logger logger = LoggerFactory.getLogger(JsonAndMap.class);

    private static Map<String,Object> generateOneLevelMap(){
        var map = new HashMap<String,Object>();
        map.put("text", "Some text");
        map.put("intValue", 1);
        map.put("longValue", 2L);
        map.put("doubleValue", 1.0);
        String[] arr1 = {"v1","v2"};
        map.put("arrOfText",arr1);
        return map;
    }

    private static Map<String,Object> generateTwoLevelMap(){
        var map = new HashMap<String,Object>();
        map.put("text2", "Some text 2");
        map.put("intValue2", 21);
        map.put("map1", generateOneLevelMap());
        
        return map;
    }

    private void test1(){
        logger.info("==>test1");
        var json = new JsonObject(generateOneLevelMap());
        logger.info("json: {}", json.encodePrettily());
        logger.info("<==test1");
    }
    
    private void test2(){
        logger.info("==>test2");
        var json = new JsonObject(generateTwoLevelMap());
        logger.info("json: {}", json.encodePrettily());
        logger.info("<==test2");
    }

    public static void main(String[] args) {
        logger.info("==>main");
        var c = new JsonAndMap();
        c.test1();
        c.test2();
        logger.info("<==main");
    }
}
