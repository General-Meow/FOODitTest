import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paulhoang.data.CustomerData;
import com.paulhoang.data.LoyaltyRules;
import com.paulhoang.services.impl.DefaultCustomerService;
import com.paulhoang.services.impl.DefaultLoyaltySystemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 01/06/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerServiceTest {
    public static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerServiceTest.class);

    private DefaultCustomerService defaultCustomerService;
    private Map<Long, CustomerData> customerStore;
    private static final Long CUSTOMER_ID_1 = 1L;
    private static final Long CUSTOMER_ID_2 = 2L;
    private static final Long CUSTOMER_ID_3 = 3L;
    private CustomerData customerData1;
    private CustomerData customerData2;
    private CustomerData customerData3;

    private static final long POINTS_TO_TRACK = 10;
    private static final List<LoyaltyRules> RULES = Lists.newArrayList();

    @Before
    public void setup()
    {
        customerStore = Maps.newHashMap();
        customerData1 = new CustomerData(CUSTOMER_ID_1);
        customerData2 = new CustomerData(CUSTOMER_ID_2);
        customerData3 = new CustomerData(CUSTOMER_ID_3);
        customerStore.put(CUSTOMER_ID_1, customerData1);
        customerStore.put(CUSTOMER_ID_2, customerData2);

        defaultCustomerService = new DefaultCustomerService();
        defaultCustomerService.setCustomerStore(customerStore);
    }

    @Test
    public void trackCustomerVisitTest()
    {
        defaultCustomerService.trackCustomerVisit(customerData1, POINTS_TO_TRACK, RULES);
        defaultCustomerService.trackCustomerVisit(customerData1, POINTS_TO_TRACK, RULES);
        defaultCustomerService.trackCustomerVisit(customerData1, POINTS_TO_TRACK, RULES);

        Assert.assertTrue("Should be 3 visits", customerData1.getVisits().size() == 3);
    }

    @Test
    public void trackCustomerVisitParamsTest()
    {
        try
        {
            defaultCustomerService.trackCustomerVisit(customerData1, -1L, RULES);
        }
        catch (final AssertionError ae)
        {
            LOG.info("Assertion failure in method call... This is expected");
            Assert.assertTrue("Should have an exception", ae != null);
        }

        try
        {
            defaultCustomerService.trackCustomerVisit(null, 1L, RULES);
        }
        catch (final AssertionError ae)
        {
            LOG.info("Assertion failure in method call... This is expected");
            Assert.assertTrue("Should have an exception", ae != null);
        }

    }

    @Test
    public void getCustomerTest(){
        final CustomerData customer = defaultCustomerService.getCustomer(CUSTOMER_ID_2);

        Assert.assertNotNull("Customer cannot be null", customer);
        Assert.assertTrue("Customer id should be " + CUSTOMER_ID_2, customer.getCustomerId() == CUSTOMER_ID_2);
    }

    @Test
    public void getCustomerNullTest(){
        final CustomerData customer = defaultCustomerService.getCustomer(12121212122L);
        Assert.assertNull("Customer should be null", customer);
    }

    @Test
    public void getCustomerParamTest(){
        try{

            final CustomerData customer = defaultCustomerService.getCustomer(-1111L);
            Assert.assertTrue(false);
        }
        catch (final AssertionError ae)
        {
            LOG.info("Assertion failure in method call... This is expected");
            Assert.assertTrue("Should have an exception", ae != null);
        }
    }

    @Test
    public void saveCustomerTest()
    {
        defaultCustomerService.saveCustomer(customerData3);
        final CustomerData customer = defaultCustomerService.getCustomer(CUSTOMER_ID_3);
        Assert.assertNotNull("Customer cannot be null", customer);
    }



}
