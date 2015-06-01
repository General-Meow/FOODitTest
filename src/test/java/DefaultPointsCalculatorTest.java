import com.google.common.collect.Lists;
import com.paulhoang.data.CustomerData;
import com.paulhoang.data.VisitData;
import com.paulhoang.services.PointsCalculator;
import com.paulhoang.services.impl.DefaultPointsCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by paul on 01/06/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPointsCalculatorTest {

    public static final Logger LOG = LoggerFactory.getLogger(DefaultPointsCalculatorTest.class);

    private PointsCalculator pointsCalculator;

    @Mock
    private CustomerData customerData1;

    @Before
    public void setup()
    {
        pointsCalculator = new DefaultPointsCalculator();
    }

    @Test
    public void calculatePointsTest()
    {
        final long points = pointsCalculator.calculatePoints(customerData1, 1.00D);
        Assert.assertTrue("points earned should be 1", points == 1L);
    }

    @Test
    public void calculatePoints3VisitsTest()
    {
        final List<VisitData> dummyList = Lists.newArrayList();
        dummyList.add(new VisitData());
        dummyList.add(new VisitData());
        when(customerData1.getVisits()).thenReturn(dummyList);
        final long points = pointsCalculator.calculatePoints(customerData1, 1.00D);
        Assert.assertTrue("points earned should be 6", points == 6L);
    }

    @Test
    public void calculatePoints3Visits100PoundsTest()
    {
        final List<VisitData> dummyList = Lists.newArrayList();
        dummyList.add(new VisitData());
        dummyList.add(new VisitData());
        when(customerData1.getVisits()).thenReturn(dummyList);
        final long points = pointsCalculator.calculatePoints(customerData1, 100.00D);
        Assert.assertTrue("points earned should be 105", points == 105L);
    }

    @Test
    public void calculatePoints3Visits101PoundsTest()
    {
        final List<VisitData> dummyList = Lists.newArrayList();
        dummyList.add(new VisitData());
        dummyList.add(new VisitData());
        when(customerData1.getVisits()).thenReturn(dummyList);
        final long points = pointsCalculator.calculatePoints(customerData1, 101.00D);
        Assert.assertTrue("points earned should be 111", points == 111L);
    }
}
