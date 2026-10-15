public class BoxParcel extends Parcel {

    private final int length;
    private final int width;
    private final int height;

    public BoxParcel(String trackingNumber, String receiverName, double weight,
                     Service service, int length, int width, int height) {

    super (trackingNumber, receiverName, weight, service);
    Validator.requireMaxWeight(weight, 50, "Box weight cannot exceed 50 kg.");
    Validator.requireAllPositive("Dimensions must be positive integers.", length, width, height);
    this.length = length;
    this.width = width;
    this.height = height;

    }

    public double getVolumetricWeight() {
        return (length * (double) width * height) / 3500.0;
    }

    @Override public String getType() {
        return "Box";
    }

    @Override public double getChargeableWeight() {
        return roundUpKg(Math.max(getWeight(), getVolumetricWeight()));
    }

    @Override protected double getBaseFee() {
        return 40.0 * getChargeableWeight();
    }

}