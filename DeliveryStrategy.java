public interface DeliveryStrategy {
    double calculateFee(double baseFee, double chargeableWeight);

    static DeliveryStrategy forService(Parcel.Service service) {
        switch (service) {
            case Standard:
                return new StandardDelivery();
            case Express:
                return new ExpressDelivery();
            case Same_day:
                return new SameDayDelivery();

            default: throw new IllegalArgumentException("Unknown");
        }
    }
    
}
