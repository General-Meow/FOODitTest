package com.paulhoang.data;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by paul on 01/06/15.
 */
public class VisitData {

    private long points;
    private Date timeStamp;
    private List<LoyaltyRules> rulesApplied;

    public VisitData()
    {
        rulesApplied = Lists.newArrayList();
        timeStamp = DateTime.now().toDate();
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(final long points) {
        this.points = points;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<LoyaltyRules> getRulesApplied() {
        return rulesApplied;
    }

    public void setRulesApplied(final List<LoyaltyRules> rulesApplied) {
        this.rulesApplied = rulesApplied;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof VisitData)) return false;

        final VisitData visitData = (VisitData) o;

        if (getPoints() != visitData.getPoints()) return false;
        if (!getTimeStamp().equals(visitData.getTimeStamp())) return false;
        return getRulesApplied().equals(visitData.getRulesApplied());

    }

    @Override
    public int hashCode() {
        int result = (int) (getPoints() ^ (getPoints() >>> 32));
        result = 31 * result + getTimeStamp().hashCode();
        result = 31 * result + getRulesApplied().hashCode();
        return result;
    }
}
