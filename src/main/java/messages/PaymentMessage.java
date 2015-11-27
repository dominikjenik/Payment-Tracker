package messages;

import java.util.regex.Pattern;

/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessage {
    private final static String cashPattern = "\\d{1,32}\\.?\\d{0,4}";
    private final static String inputPatternString =
            "^([A-Z]{3})\\ *([+-]?(" + cashPattern + "))\\ *(\\(USD\\ *" + cashPattern + "\\)){0,1}$";
    public final static Pattern inputPattern = Pattern.compile(inputPatternString);
    private final String message;

    public PaymentMessage(String message) throws NotMatchPaymentPatternMessage {
        if (!inputPattern.matcher(message).find()) {
            throw new NotMatchPaymentPatternMessage(message);
        }
        this.message = message;

    }

    public String getMessage() {
        return message;
    }
}
