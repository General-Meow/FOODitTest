package com.paulhoang.services;

import com.google.common.collect.Maps;
import com.paulhoang.data.CustomerData;
import com.paulhoang.data.LoyaltyRules;
import com.paulhoang.data.VisitData;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 31/05/15.
 */
public interface PointsCalculator {

    long calculatePoints(CustomerData customerData, double moneySpent);
    List<LoyaltyRules> calculateRules(CustomerData customerData, double moneySpent);
    double calculateValue(List<VisitData> visits);
}
