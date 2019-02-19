/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pverts.restserver1.data;

import java.util.Objects;

/**
 * @author Yuriy Stul
 */
public class ServiceStatus {
    private String name;
    private String id;
    private String status;

    public ServiceStatus(String name, String id, String status) {
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceStatus)) return false;
        ServiceStatus that = (ServiceStatus) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getStatus(), that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId(), getStatus());
    }
}
