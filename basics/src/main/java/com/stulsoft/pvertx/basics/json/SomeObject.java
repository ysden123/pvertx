/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.basics.json;

import java.util.Objects;

/**
 * @author Yuriy Stul
 */
public class SomeObject {
    private String name;
    private Integer age;

    public SomeObject(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SomeObject)) return false;
        SomeObject that = (SomeObject) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAge(), that.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge());
    }

    @Override
    public String toString() {
        return "SomeObject{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
