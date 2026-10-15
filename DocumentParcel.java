public class DocumentParcel extends Parcel {
    private final int pages;

    public DocumentParcel(String trackingNumber, String receiverName, double weight,
                          Service service, int pages) {

    super(trackingNumber, receiverName, weight, service);
    Validator.requireInRange(pages, 1, 100, "Number of pages must be from 1 to 100.");
    Validator.requireMaxWeight(weight, 0.5, "Document maximum weight is 0.5 kg.");
    this.pages = pages;

    }

    @Override public String getType() {
        return "Document";
    }

    @Override public double getChargeableWeight() {
        return getWeight();
    }

    @Override protected double getBaseFee() {
        return 50.0;
    }

    public int getPages() {
        return pages;
    }
}
