// RideMatchId.java
package com.example.demo.Models.EntityClasses;

import java.io.Serializable;
import java.util.Objects;
public class RideMatchId implements Serializable {
    private String driverOfferId;
    private String riderRequestId;

    public RideMatchId() {}

    public RideMatchId(String driverOfferId, String riderRequestId) {
        this.driverOfferId = driverOfferId;
        this.riderRequestId = riderRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RideMatchId that)) return false;
        return Objects.equals(driverOfferId, that.driverOfferId) &&
                Objects.equals(riderRequestId, that.riderRequestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverOfferId, riderRequestId);
    }
}