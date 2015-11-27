package messages;

import java.util.regex.Pattern;

/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessage {
    private final static int DIGITS_BEFORE_DOT = 32;
    private static final int DIGITS_AFTER_DOT = 4;
    private final static String CASH_PATTERN = "\\d{1," + DIGITS_BEFORE_DOT + "}\\.?\\d{0," + DIGITS_AFTER_DOT + "}";
    private final static String INPUT_PATTERN_STRING =
            "^([A-Z]{3})\\ *([+-]?(" + CASH_PATTERN + "))\\ *(\\(USD\\ *(" + CASH_PATTERN + ")\\))?$";
    public final static Pattern inputPattern = Pattern.compile(INPUT_PATTERN_STRING);
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
