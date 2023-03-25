package entity;

import java.util.Map;
import java.util.TreeMap;

public class Tariff {
    private final String id;
    private int minutes;
    private double cost;

    private static final String[] CALL_TYPES = {
            "01",
            "02"
    };

    private static final Map<String, Integer> MINUTES_MAP = new TreeMap<>();
    private static final Map<String, Double> COST_MAP = new TreeMap<>();
    static {
        MINUTES_MAP.put("03", 0);
        MINUTES_MAP.put("06", 300);
        MINUTES_MAP.put("11", 100);
        COST_MAP.put("03", 1.5);
        COST_MAP.put("06", 1.);
        COST_MAP.put("11", 0.5);
    }

    public Tariff(String id) {
        try {
            this.id = id;
            this.minutes = MINUTES_MAP.get(id);
            this.cost = COST_MAP.get(id);
        } catch (NullPointerException e) {
            throw new RuntimeException("Invalid tariff id!");
        }
    }

    public String getId() {
        return id;
    }

    public double calculateCost(int call_minutes, String callType) {
        switch (id) {
            case "03": {
                return cost * call_minutes;
            }
            case "06": {
                int spentMinutes = Math.min(call_minutes, this.minutes);
                this.minutes -= spentMinutes;
                return (call_minutes - spentMinutes) * cost;
            }
            case "11": {
                if (callType.equals(CALL_TYPES[0])) {
                    int spentMinutes = Math.min(call_minutes, this.minutes);
                    this.minutes -= spentMinutes;
                    if (this.minutes == 0) {
                        double prevCost = cost;
                        cost = COST_MAP.get("03");
                        return spentMinutes * prevCost + (call_minutes - spentMinutes) * cost;
                    }
                    return call_minutes * cost;
                }
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
}
