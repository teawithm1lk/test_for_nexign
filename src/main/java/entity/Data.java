package entity;

import java.util.Date;

public class Data implements Comparable<Data>{
    private final String callType;
    private final Date startTime;
    private final Date endTime;
    private final long duration;
    private final double cost;

    private static final int NUMBER_CONVERT_TO_MINUTES = 1000 * 60;

    public Data(String callType, Date startTime, Date endTime, User user) {
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime.getTime() - startTime.getTime();
        this.cost = user.calculateCost((int)Math.ceil((double)duration / NUMBER_CONVERT_TO_MINUTES), callType);
    }

    public String getCallType() {
        return callType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public int compareTo(Data o) {
        return startTime.compareTo(o.getStartTime());
    }
}
