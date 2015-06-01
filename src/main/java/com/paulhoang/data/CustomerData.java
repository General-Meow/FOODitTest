package com.paulhoang.data;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by paul on 31/05/15.
 */
public class CustomerData {

    private long customerId;
    private List<VisitData> visits;


    public CustomerData()
    {
        customerId = 0L;
        visits = Lists.newArrayList();
    }

    public CustomerData(final long customerId)
    {
        this();
        this.customerId = customerId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final long customerId) {
        this.customerId = customerId;
    }

    public List<VisitData> getVisits() {
        return visits;
    }

    public void setVisits(final List<VisitData> visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerData)) return false;

        final CustomerData that = (CustomerData) o;

        if (getCustomerId() != that.getCustomerId()) return false;
        return getVisits().equals(that.getVisits());

    }

    @Override
    public int hashCode() {
        int result = (int) (getCustomerId() ^ (getCustomerId() >>> 32));
        result = 31 * result + getVisits().hashCode();
        return result;
    }
}
