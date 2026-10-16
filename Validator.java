public final class Validator {
    private static final String TRACKING_PATTERN = "[A-Z]{3}\\d{4}";

    private Validator() { 

    }

    public static String requireTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || !trackingNumber.matches(TRACKING_PATTERN))
            throw new IllegalArgumentException("Tracking number must follow ABC0000 format.");
        return trackingNumber;
    }

    public static String requireNonBlank(String value, String message) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(message);
        return value.trim();
    }

    public static String requireValidName(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("\nName cannot be empty.");
        if (!value.matches("[a-zA-Z ]+"))
            throw new IllegalArgumentException("\nName can only contain letters and spaces.");
        return value.trim();
    }

    public static double requirePositiveWeight(double weight) {
        if (!Double.isFinite(weight) || weight <= 0)
            throw new IllegalArgumentException("Weight must be greater than 0.");
        return weight;
    }

    public static void requireMaxWeight(double weight, double max, String message) {
        if (weight > max) throw new IllegalArgumentException(message);
    }

    public static void requireInRange(int value, int min, int max, String message) {
        if (value < min || value > max) throw new IllegalArgumentException(message);
    }

    public static void requireAllPositive(String message, int... values) {
        for (int v : values) {
            if (v <= 0) throw new IllegalArgumentException(message);
        }
    }

    public static <T> T requireNonNull(T value, String message) {
        if (value == null) throw new IllegalArgumentException(message);
        return value;
    }

    public static int parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Please enter a valid whole number.");
        }
    }

    public static double parseDouble(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }
}