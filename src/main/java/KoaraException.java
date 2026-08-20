// Represents an error caused by an invalid command entered into Koara.
public class KoaraException extends Exception {
    // Creates a Koara-specific exception containing a user-facing error message.
    public KoaraException(String message) {
        super(message);
    }
}
