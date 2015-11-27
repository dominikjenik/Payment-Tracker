package messages;

/**
 * Created by Jenik on 11/27/2015.
 */
public class NotMatchPaymentPatternMessage extends Throwable {
    private final String message;

    public NotMatchPaymentPatternMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
