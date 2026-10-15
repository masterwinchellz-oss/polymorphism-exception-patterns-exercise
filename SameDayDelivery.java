public class SameDayDelivery implements DeliveryStrategy {

    @Override public double calculateFee(double baseFee, double chargableWeight) {
        if (chargableWeight > 10)
            throw new IllegalArgumentException("Same day delivery is only available for parcels up to 10 Kg.");
        return baseFee * 2 + 100.0;
    }
    
}
