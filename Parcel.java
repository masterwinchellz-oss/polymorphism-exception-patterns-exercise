public abstract class Parcel {
    public enum Status { Pending, Delivered }
    public enum Service { Standard, Express, Same_day }

    private final String trackingNumber;
    private final String receiverName;
    private final double weight;
    private Service service;
    private Status status = Status.Pending;

    public Parcel(String trackingNumber, String receiverName, double weight, Service service) {
        this.trackingNumber = Validator.requireTrackingNumber(trackingNumber);
        this.receiverName = Validator.requireNonBlank(receiverName, "Receiver name cannot be empty.");
        this.weight = Validator.requirePositiveWeight(weight);
        this.service = Validator.requireNonNull(service, "Service cannot be null.");
    }

    public abstract String getType();
    public abstract double getChargeableWeight();
    protected abstract double getBaseFee();

    public double getDeliveryFee() {
        return DeliveryStrategy.forService(service).calculateFee(getBaseFee(), getChargeableWeight());
    }

    public void changeService(Service newService) {
        if (status == Status.Delivered)
            throw new IllegalStateException("Parcel already delivered.");
        Validator.requireNonNull(newService, "Delivery service is required.");

        DeliveryStrategy.forService(newService).calculateFee(getBaseFee(), getChargeableWeight());
        service = newService;
    }

    public void markDelivered() {
        if (status == Status.Delivered) throw new IllegalStateException("Parcel was already delivered.");
        status = Status.Delivered;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }
    public String getReceiverName() {
        return receiverName;
    }

    public double getWeight() {
        return weight;
    }

    public Service getService() {
        return service;
    }

    public Status getStatus() {
        return status;
    }

    protected static double roundUpKg(double weight) {
        return Math.ceil(weight);
    }

}

