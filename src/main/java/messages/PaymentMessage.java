package messages;

/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessage {
    public final static String cashPattern="\\d{1,32}\\.?\\d{0,4}";
    public final static String inputPattern =
            "^([A-Z]{3})\\ *[+-]?("+cashPattern+")\\ *(\\(USD\\ *"+cashPattern+"\\)){0,1}$";
    private final String message;

    public PaymentMessage(String message) throws NotMatchPaymentPatternMessage {
        if (!message.matches(inputPattern)) {
            throw new NotMatchPaymentPatternMessage(message);
        }
        this.message = message;

    }

    public String getMessage() {
        return message;
    }
}
