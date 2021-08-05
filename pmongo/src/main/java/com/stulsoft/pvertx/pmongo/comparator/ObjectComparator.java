/*
 * Copyright (c) 2021. Webpals
 */

package com.stulsoft.pvertx.pmongo.comparator;

/**
 * @author Yuriy Stul
 */
public class ObjectComparator {

    public static boolean gte(Object o1, Object o2) throws Exception{
        try{
            var l1 = Long.parseLong(o1.toString());
            var l2 = Long.parseLong(o2.toString());
            return l1 >= l2;
        }catch(Exception __){
            return false;
        }
    }

    public static boolean lte(Object o1, Object o2) throws Exception{
        try{
            var l1 = Long.parseLong(o1.toString());
            var l2 = Long.parseLong(o2.toString());
            return l1 <= l2;
        }catch(Exception __){
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            var i1 = Integer.valueOf(123);
            var i2 = Integer.valueOf(200);
            var l1 = Long.valueOf(123);
            System.out.printf("%d => %d: %s %n", i1,i2,gte(i1, i2));
            System.out.printf("%d => %d: %s %n", i2,i1,gte(i2, i1));
            System.out.printf("%d => %d: %s %n", l1,i2,gte(l1, i2));

            System.out.printf("%d <= %d: %s %n", i1,i2,lte(i1, i2));
            System.out.printf("%d <= %d: %s %n", i2,i1,lte(i2, i1));
            System.out.printf("%d <= %d: %s %n", l1,i2,lte(l1, i2));
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
