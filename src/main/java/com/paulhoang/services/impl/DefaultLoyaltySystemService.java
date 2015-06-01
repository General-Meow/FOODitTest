package com.paulhoang.services.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paulhoang.data.CustomerData;
import com.paulhoang.data.LoyaltyRules;
import com.paulhoang.data.VisitData;
import com.paulhoang.services.CustomerService;
import com.paulhoang.services.LoyaltySystemService;
import com.paulhoang.services.PointsCalculator;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 31/05/15.
 */
public class DefaultLoyaltySystemService implements LoyaltySystemService{

    public static final Logger LOG = LoggerFactory.getLogger(DefaultLoyaltySystemService.class);

    private CustomerService customerService;
    private PointsCalculator pointsCalculator;

    public DefaultLoyaltySystemService()
    {
        //should use DI - too tightly coupled
        customerService = new DefaultCustomerService();
        pointsCalculator = new DefaultPointsCalculator();
    }

    /**
     * assume that if the customer doesn't exist, create
     * @param customerId
     * @param moneySpent
     */
    @Override
    public void spendMoney(final long customerId, final double moneySpent) {
        Assert.assertTrue("customerId must be > 0", customerId > 0L);
        Assert.assertTrue("moneySpent must be > 0.0", moneySpent > 0.0D);

        //find customer
        CustomerData customer = customerService.getCustomer(customerId);

        if(customer == null)
        {
            customer = new CustomerData(customerId);
        }

        final long pointsEarned = pointsCalculator.calculatePoints(customer, moneySpent);
        final List<LoyaltyRules> loyaltyRules = pointsCalculator.calculateRules(customer, moneySpent);
        customerService.trackCustomerVisit(customer, pointsEarned, loyaltyRules);

        //save
        customerService.saveCustomer(customer);
    }

    @Override
    public void calculateStatement(final long customerId) {
        final CustomerData customer = customerService.getCustomer(customerId);

        if(customer != null)
        {
            LOG.info("Customer {} statement", customerId);
            final Map<Integer, Statement> monthStatements =
                    organiseStatements(customer.getVisits());

            final DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

            for(final Map.Entry<Integer, Statement> entry : monthStatements.entrySet())
            {
                final Statement statement = entry.getValue();
                LOG.info("Month: {}", statement.getMonth());
                LOG.info("Visits in total: {}", statement.getVisits().size());
                LOG.info("Visits:");
                for(final VisitData visitData : statement.getVisits())
                {
                    final DateTime dateTime = new DateTime(visitData.getTimeStamp());
                    LOG.info("{} {}", dateTime.toString(dtf), StringUtils.join(visitData.getRulesApplied()), ", ");
                }
                LOG.info("Worth: {}", pointsCalculator.calculateValue(statement.getVisits()));
            }

        }else {
            LOG.info("Customer {} not found", customerId);
        }
    }

    //yuk
    protected Map<Integer, Statement> organiseStatements(final List<VisitData> visits)
    {
        final Map<Integer, Statement> monthStatements = Maps.newTreeMap();
        if(visits != null && visits.size() > 0) {
            for (final VisitData visitData : visits) {
                final DateTime visitDate = new DateTime(visitData.getTimeStamp());
                final String month = visitDate.toString("MMM");

                Statement statement = monthStatements.get(visitDate.getMonthOfYear());
                if(statement == null)
                {
                    statement = new Statement();
                    statement.setVisits(Lists.<VisitData>newArrayList());
                }
                statement.setMonth(month);
                statement.getVisits().add(visitData);
                monthStatements.put(visitDate.getMonthOfYear(), statement);
            }

            boolean first = true;
            long lastMonthsPoints = 0L;
            for(final Map.Entry<Integer, Statement> entry : monthStatements.entrySet())
            {
                final Statement statement = entry.getValue();
                if(first)
                {
                    statement.setPointsStartedWith(0L);
                    first = false;
                }
                statement.setPointsStartedWith(lastMonthsPoints);

                long pointsCounter = 0L;
                for(final VisitData visitData : statement.getVisits())
                {
                    pointsCounter += visitData.getPoints();
                }
                statement.setPointsEndedWith(pointsCounter);
            }
        }


        return monthStatements;
    }

    class Statement{
        private String month;
        private List<VisitData> visits;
        private long pointsStartedWith;
        private long pointsEndedWith;

        public String getMonth() {
            return month;
        }

        public void setMonth(final String month) {
            this.month = month;
        }

        public List<VisitData> getVisits() {
            return visits;
        }

        public void setVisits(final List<VisitData> visits) {
            this.visits = visits;
        }

        public long getPointsStartedWith() {
            return pointsStartedWith;
        }

        public void setPointsStartedWith(final long pointsStartedWith) {
            this.pointsStartedWith = pointsStartedWith;
        }

        public long getPointsEndedWith() {
            return pointsEndedWith;
        }

        public void setPointsEndedWith(final long pointsEndedWith) {
            this.pointsEndedWith = pointsEndedWith;
        }
    }
}
