package com.paulhoang.services;

import com.paulhoang.data.CustomerData;
import com.paulhoang.data.LoyaltyRules;

import java.util.List;

/**
 * Created by paul on 31/05/15.
 */
public interface CustomerService {

    CustomerData getCustomer(long customerId);

    void saveCustomer(CustomerData customerData);

    void trackCustomerVisit(CustomerData customerData, long points, List<LoyaltyRules> loyaltyRules);

}
