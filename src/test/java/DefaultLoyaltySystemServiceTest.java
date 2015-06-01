import com.paulhoang.services.LoyaltySystemService;
import com.paulhoang.services.impl.DefaultLoyaltySystemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paul on 31/05/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class DefaultLoyaltySystemServiceTest
{
    public static final Logger LOG = LoggerFactory.getLogger(DefaultLoyaltySystemServiceTest.class);
    private static final long CUSTOMER_ID_1 = 1L;

    private LoyaltySystemService defaultLoyaltySystemService;

    @Before
    public void setup()
    {
        defaultLoyaltySystemService = new DefaultLoyaltySystemService();
    }

    @Test
    public void spendMoneyTest()
    {
        defaultLoyaltySystemService.spendMoney(CUSTOMER_ID_1, 1.00D);
    }

    @Test
    public void printStatementNoCustomersTest()
    {
        defaultLoyaltySystemService.calculateStatement(CUSTOMER_ID_1);
    }

    @Test
    public void printStatementTest()
    {
        LOG.info("Start printing statement for one visit");
        defaultLoyaltySystemService.spendMoney(CUSTOMER_ID_1, 1.00D);
        defaultLoyaltySystemService.calculateStatement(CUSTOMER_ID_1);
        LOG.info("End printing statement for one visit");
    }

    @Test
    public void printStatementMoreVisitsTest()
    {
        LOG.info("Start printing statement for three visit");
        defaultLoyaltySystemService.spendMoney(CUSTOMER_ID_1, 1.00D);
        defaultLoyaltySystemService.spendMoney(CUSTOMER_ID_1, 101.00D);
        defaultLoyaltySystemService.spendMoney(CUSTOMER_ID_1, 111.00D);
        defaultLoyaltySystemService.calculateStatement(CUSTOMER_ID_1);
        LOG.info("End printing statement for three visit");

    }

}
