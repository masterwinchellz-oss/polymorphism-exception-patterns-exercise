import java.util.Scanner;

public class Main{
    private static final Scanner scanner = new Scanner(System.in);
    private static final ParcelManager manager = ParcelManager.getInstance();

    public static void main (String [] args){

        boolean isRunning = true;
        int choice;

        while (isRunning) {
            System.out.println("\n==Parcel Delivery System==");
            System.out.println("1) Add Parcel");
            System.out.println("2) View All Parcel");
            System.out.println("3) Change Delivery Service");
            System.out.println("4) Mark as Delivered");
            System.out.println("5) Exit");

            boolean isChoiceValid = false;
            choice = 0;

            while (!isChoiceValid){
                System.out.print("Enter your choice: ");
                String input = scanner.nextLine().trim();

                if (!input.matches("[1-5]")){
                    System.out.println("\nInvalid input. Please Choose 1-5.");
                    continue;
                }
                choice = Integer.parseInt(input);
                isChoiceValid = true;
            }

            try {
                switch (choice){
                    case 1:
                        addParcel();
                        break;
                    case 2:
                        viewAllParcels();
                        break;
                    case 3:
                        changeService();
                        break;
                    case 4:
                        markDelivered();
                        break;
                    case 5:
                        isRunning = false;
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a number between 1 and 5.");
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException ex){
                System.out.println(ex.getMessage());
            }

        }
        scanner.close();
    }

    private static void addParcel() {

    System.out.println("\nParcel Type Options:");
    System.out.println("1) Document");
    System.out.println("2) Box");
    System.out.println("3) Fragile");

    int type = readIntInRange("\nChoose type: ", 1, 3);

    // Tracking number
    String tracking = "";
    boolean isValid = false;
    while (!isValid) {
        try {
            tracking = readLine("\nEnter tracking number (ABC0000): ").toUpperCase();
            Validator.requireTrackingNumber(tracking);
            if (manager.findByTrackingNumber(tracking) != null)
                throw new IllegalArgumentException("Duplicate tracking number!");
            isValid = true;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Receiver name
    String receiver = "";
    isValid = false;
    while (!isValid) {
        try {
            receiver = Validator.requireValidName(readLine("Enter receiver name: "));
            isValid = true;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Weight 
    double maxWeight = (type == 1) ? 0.5 : (type == 2) ? 50 : 30;
    double weight = 0;
    isValid = false;
    while (!isValid) {
        try {
            weight = Validator.requirePositiveWeight(readDouble("Enter actual weight (kg): "));
            Validator.requireMaxWeight(weight, maxWeight, "Weight cannot exceed " + maxWeight + " kg.");
            isValid = true;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Type-specific details
    int pages = 0, length = 0, width = 0, height = 0;
    String note = "";

    switch (type) {
        case 1:
            pages = readIntInRange("Enter number of pages (1-100): ", 1, 100);
            break;

        case 2:
        case 3:
            length = readIntInRange("Enter length (cm): ", 1, 300);
            width  = readIntInRange("Enter width (cm): ", 1, 300);
            height = readIntInRange("Enter height (cm): ", 1, 300);

            if (type == 3) {
                isValid = false;
                while (!isValid) {
                    try {
                        note = Validator.requireNonBlank(readLine("Enter handling notes: "),
                                "Handling note cannot be empty.");
                        isValid = true;
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
            break;
    }

    Parcel parcel = null;
    isValid = false;
    while (!isValid) {
        try {
            Parcel.Service service = readService();
            Parcel candidate;

            if (type == 1) {
                candidate = new DocumentParcel(tracking, receiver, weight, service, pages);
            } else if (type == 2) {
                candidate = new BoxParcel(tracking, receiver, weight, service, length, width, height);
            } else {
                candidate = new FragileParcel(tracking, receiver, weight, service, length, width, height, note);
            }

            manager.addParcel(candidate);   
            parcel = candidate;
            isValid = true;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    System.out.printf("\nParcel added successfully! Delivery fee: Php %.2f%n", parcel.getDeliveryFee());
}

    private static void viewAllParcels() {
        if (!hasParcels()) return;
        System.out.printf("\n%-12s %-10s %-20s %-12s %-12s %-12s %-10s%n",
                "Tracking", "Type", "Receiver", "Charge kg", "Service", "Fee (Php)", "Status");
        for (Parcel parcel : manager.getParcels()) {
            System.out.printf("%-12s %-10s %-20s %-12.2f %-12s %-12.2f %-10s%n",
                    parcel.getTrackingNumber(),
                    parcel.getType(),
                    parcel.getReceiverName(),
                    parcel.getChargeableWeight(),
                    parcel.getService(),
                    parcel.getDeliveryFee(),
                    parcel.getStatus());
        }
    }

    private static void changeService() {
        if (!hasParcels()) return;

        String tracking = readLine("Tracking number: ").toUpperCase();
        Parcel parcel = manager.findByTrackingNumber(tracking);
        if (parcel == null) {
            System.out.print("\nParcel not found.");
            return;
        }

        if (parcel.getStatus() == Parcel.Status.Delivered) {
            System.out.print("\nParcel already delivered.");
            return;
        }

        Parcel.Service newService = readService();
        parcel.changeService(newService);
        System.out.printf("Delivery service changed! New fee: Php %.2f%n", parcel.getDeliveryFee());
        
    }

    private static void markDelivered() {
        if (!hasParcels()) return;
        
        String tracking = readLine("Tracking number: ").toUpperCase();
        Parcel parcel = manager.findByTrackingNumber(tracking);
        if (parcel == null) {
            System.out.print("Parcel not found.");
            return;
        }
        
        if (parcel.getStatus() == Parcel.Status.Delivered) {
            System.out.print("Parcel already delivered.");
            return;
        }
        parcel.markDelivered();
        System.out.println("Parcel delivered successfully!");

    }

    private static Parcel.Service readService() {
        System.out.println("Delivery Service Options:");
        System.out.println("1) Standard");
        System.out.println("2) Express");
        System.out.println("3) Same Day");

        int serviceChoice = readInt("Choose service: ");
        switch (serviceChoice) {
            case 1:
                return Parcel.Service.Standard;
            case 2:
                return Parcel.Service.Express;
            case 3:
                return Parcel.Service.Same_day;
            default:
                throw new IllegalArgumentException("Invalid service choice.");
        }

    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        return Validator.parseInt(readLine(prompt));
    }

    private static double readDouble(String prompt) {
        return Validator.parseDouble(readLine(prompt));
    }

    private static boolean hasParcels() {
    if (manager.getParcels().isEmpty()) {
        System.out.println("\n--No Parcels found.--");
        return false;
    }
    return true;
  }

    private static int readIntInRange(String prompt, int min, int max) {
    int value = 0;
    boolean isValid = false;

    while (!isValid) {
        try {
            value = readInt(prompt);
            if (value < min || value > max)
                throw new IllegalArgumentException("Please enter a number from " + min + " to " + max + ".");
            isValid = true;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            }
        }
        return value;
    }

}