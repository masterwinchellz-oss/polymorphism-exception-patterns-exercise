public class FragileParcel extends BoxParcel {

    private final String handlingNote;

    public FragileParcel(String trackingNumber, String receiverName, double weight, Service service, int length, 
                        int width, int height, String handlingNote) {

        super(trackingNumber, receiverName, weight, service, length, width, height);
        Validator.requireMaxWeight(weight, 30, "Fragile parcel maximum weight is 30 Kg.");
        this.handlingNote = Validator.requireNonBlank(handlingNote, "Handling note cannot be empty.");

    }

    @Override public String getType() {
        return "Fragile";
    }

    @Override protected double getBaseFee() {
        return super.getBaseFee() + 100.0;
    }

    public String getHandlingNote() {
        return handlingNote;
    }
    
}
