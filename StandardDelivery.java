public class StandardDelivery implements DeliveryStrategy {

    @Override public double calculateFee(double baseFee, double chargeableWeight) {
        return baseFee;
    }
}

