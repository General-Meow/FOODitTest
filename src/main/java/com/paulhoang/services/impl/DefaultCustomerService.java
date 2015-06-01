package com.paulhoang.services.impl;

import com.google.common.collect.Maps;
import com.paulhoang.data.CustomerData;
import com.paulhoang.data.LoyaltyRules;
import com.paulhoang.data.VisitData;
import com.paulhoang.services.CustomerService;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 31/05/15.
 */
public class DefaultCustomerService implements CustomerService {

    private Map<Long, CustomerData> customerStore;

    public DefaultCustomerService() {
        this.customerStore = Maps.newHashMap();
    }

    public Map<Long, CustomerData> getCustomerStore() {
        return customerStore;
    }

    public void setCustomerStore(final Map<Long, CustomerData> customerStore) {
        this.customerStore = customerStore;
    }

    public CustomerData getCustomer(final long customerId)
    {
        Assert.assertTrue("customerId must be > 0", customerId > 0L);

        return getCustomerStore().containsKey(customerId) ? getCustomerStore().get(customerId) : null;
    }

    public void saveCustomer(final CustomerData customerData)
    {
        Assert.assertNotNull("customerData cannot be null", customerData != null);
        getCustomerStore().put(customerData.getCustomerId(), customerData);
    }

    @Override
    public void trackCustomerVisit(final CustomerData customerData, final long points, final List<LoyaltyRules> loyaltyRules) {
        Assert.assertNotNull("customerData cannot be null", customerData);
        Assert.assertNotNull("loyaltyRules cannot be null", loyaltyRules);
        Assert.assertTrue("points need to be equal or greater than 0", points >= 0L);

        final VisitData visitData = new VisitData();
        visitData.setTimeStamp(DateTime.now().toDate());
        visitData.setPoints(points);
        visitData.setRulesApplied(loyaltyRules);

        customerData.getVisits().add(visitData);
    }
}
