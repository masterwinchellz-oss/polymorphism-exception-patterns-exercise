import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParcelManager {

    private static final ParcelManager Instance = new ParcelManager();
    private final List<Parcel> parcels = new ArrayList<>();

    private ParcelManager() {

    }

    public static ParcelManager getInstance() {
        return Instance;
    }

    public void addParcel(Parcel parcel) {
        if (parcel == null) 
            throw new IllegalArgumentException("Parcel cannot be Empty.");

        if (findByTrackingNumber(parcel.getTrackingNumber()) != null)
            throw new IllegalArgumentException("Duplicate tracking number!");

        parcel.getDeliveryFee();
        parcels.add(parcel);
    }

    public Parcel findByTrackingNumber(String trackingNumber) {
        for (Parcel parcel : parcels) {
            if (parcel.getTrackingNumber().equalsIgnoreCase(trackingNumber))
                return parcel;
        }
        return null;
    }

    public List<Parcel> getParcels() {
        return Collections.unmodifiableList(parcels);
    }
        
    
}
