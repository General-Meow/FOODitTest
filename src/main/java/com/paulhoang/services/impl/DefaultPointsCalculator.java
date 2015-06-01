package com.paulhoang.services.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paulhoang.data.CustomerData;
import com.paulhoang.data.LoyaltyRules;
import com.paulhoang.data.VisitData;
import com.paulhoang.services.PointsCalculator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 31/05/15.
 */
public class DefaultPointsCalculator implements PointsCalculator {

    public static final double BASE_POINTS_WORTH = 0.02D;
    public static final int BASE_POINTS_FOR_POUND = 1;
    public static final int BONUS_POINTS_FOR_100_SPENT = 5;
    public static final int VISITS_REQUIRED_FOR_BONUS = 3;
    public static final int BONUS_POINTS_FOR_VISITS = 5;
    public static final int POINTS_MULTIPLIER_FOR_EARLY_ORDER = 2;
    public static final int POINTS_MULTIPLIER_CUTOFF_HOUR = 18;


    @Override
    public long calculatePoints(final CustomerData customerData, final double moneySpent) {

        long pointsEarned = 0;
        pointsEarned += calculatedBasePoints(moneySpent);
        pointsEarned += calculatedBonusPoints(customerData, moneySpent);

        return pointsEarned;
    }



    protected long calculatedBasePoints(final double moneySpent)
    {
        final double penceSpent = moneySpent % 1;
        final double poundsSpent = moneySpent - penceSpent;

        return (long) poundsSpent * BASE_POINTS_FOR_POUND;
    }


    /**
     * bonus points are assumed to be compound
     * assumed that customer visit data has NOT yet been updated
     * @param moneySpent
     * @return
     */
    protected long calculatedBonusPoints(final CustomerData customerData, final double moneySpent)
    {
        long bonusPoints = 0;

        final double penceSpent = moneySpent % 1;
        final double poundsSpent = moneySpent - penceSpent;

        bonusPoints += poundsSpent > 100 ? BONUS_POINTS_FOR_100_SPENT : 0;

        bonusPoints += (customerData.getVisits().size()+1) % VISITS_REQUIRED_FOR_BONUS == 0 ? BONUS_POINTS_FOR_VISITS : 0;

        final DateTime now = DateTime.now();
        final int dayOfWeek = now.getDayOfWeek();

        if(DateTimeConstants.TUESDAY == dayOfWeek || DateTimeConstants.WEDNESDAY == dayOfWeek) {
            bonusPoints *= now.getHourOfDay() < POINTS_MULTIPLIER_CUTOFF_HOUR ? POINTS_MULTIPLIER_FOR_EARLY_ORDER : 1;
        }
        return bonusPoints;
    }

    @Override
    public List<LoyaltyRules> calculateRules(final CustomerData customerData, final double moneySpent) {
        final List<LoyaltyRules> rules = Lists.newArrayList();
        final double penceSpent = moneySpent % 1;
        final double poundsSpent = moneySpent - penceSpent;

        if(poundsSpent > 100)
        {
            rules.add(LoyaltyRules.OVER_100);
        }

        if((customerData.getVisits().size()+1) % VISITS_REQUIRED_FOR_BONUS == 0)
        {
            rules.add(LoyaltyRules.THIRED_VISIT);
        }

        final DateTime now = DateTime.now();
        final int dayOfWeek = now.getDayOfWeek();
        if(DateTimeConstants.TUESDAY == dayOfWeek || DateTimeConstants.WEDNESDAY == dayOfWeek) {
            if (now.getHourOfDay() < POINTS_MULTIPLIER_CUTOFF_HOUR)
            {
                rules.add(LoyaltyRules.DOUBLE_POINTS);
            }
        }

        return rules;
    }

    @Override
    public double calculateValue(final List<VisitData> visits) {
        double value = 0.0D;
        for(final VisitData visit : visits)
        {
            value += visit.getPoints() * BASE_POINTS_WORTH;
        }

        return value;
    }
}
