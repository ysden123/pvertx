/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.message.coding;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Demonstrates JsonObject serialization/deserialization
 *
 * @author Yuriy Stul
 */
public class SomeObject {
    private String name;
    private int value;
    private List<String> items = new ArrayList<>();

    /**
     * Used by JsonObject
     */
    private SomeObject() {
    }

    public SomeObject(String name, int value) {
        setName(name);
        setValue(value);
    }

    public static SomeObject fromJson(final JsonObject json) {
        return json.mapTo(SomeObject.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<String> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SomeObject)) return false;
        SomeObject that = (SomeObject) o;
        return getValue() == that.getValue() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue(), getItems());
    }

    @Override
    public String toString() {
        return "SomeObject{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", items=" + items +
                '}';
    }

    public static void main(String[] args) {
        System.out.println("==>main");

        var someObject = new SomeObject("name 1", 123);
        someObject.setItems(Arrays.asList("111", "2222", "3333"));
        var json = someObject.toJson();
        var restoredSomeObject = SomeObject.fromJson(json);
        assert (someObject.equals(restoredSomeObject));

        System.out.printf("restoredSomeObject: %s%n", restoredSomeObject.toString());
        System.out.println("<==main");
    }
}
