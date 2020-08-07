/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx.pchain;

/**
 * @author Yuriy Stul
 */
public interface Utils {

    static String[] generateMsg(int n){
        var msgs = new String[n];
        for(var i = 0; i < n; ++i){
            msgs[i] = String.format("message %d", i + 1);
        }

        return msgs;
    }
}
