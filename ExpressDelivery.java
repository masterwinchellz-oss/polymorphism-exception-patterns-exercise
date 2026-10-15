public class ExpressDelivery implements DeliveryStrategy {

    @Override public double calculateFee(double baseFee, double chargeableWeight) {
        return baseFee * 1.5;
    }
    
}
