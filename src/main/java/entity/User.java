package entity;

public class User implements Comparable<User> {
    private final String phoneNumber;
    private final Tariff tariff;

    public User(String phoneNumber, String idTariff) {
        this.phoneNumber = phoneNumber;
        this.tariff = new Tariff(idTariff);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public double calculateCost(int minutes, String callType) {
        return tariff.calculateCost(minutes, callType);
    }

    @Override
    public int compareTo(User user) {
        return phoneNumber.compareTo(user.getPhoneNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return phoneNumber.equals(((User)o).getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
