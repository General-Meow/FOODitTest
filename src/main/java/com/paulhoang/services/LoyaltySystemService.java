package com.paulhoang.services;

/**
 * Created by paul on 31/05/15.
 */
public interface LoyaltySystemService {

    void spendMoney(long customerId, double moneySpent);

    void calculateStatement(long customerId);
}
