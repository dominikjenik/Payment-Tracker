package messages;

import extraction.PaymentMessageMatcher;

import java.util.regex.Pattern;

/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessage {
    public final static int DIGITS_BEFORE_DOT = 32;
    public final static int DIGITS_AFTER_DOT = 4;
    private final static String CASH_PATTERN = "(?=.*[1-9])\\d{1," + DIGITS_BEFORE_DOT + "}(\\.\\d{0," + DIGITS_AFTER_DOT + "})?";
    private final static String INPUT_PATTERN_STRING =
            "^(?!USD.*USD)([A-Z]{3})\\ *([+-]?(" + CASH_PATTERN + "))\\ *(\\(USD\\ *(" + CASH_PATTERN + ")\\))?$";
    public final static Pattern inputPattern = Pattern.compile(INPUT_PATTERN_STRING);
    private final String message;
    private boolean saveToFile=true;

    PaymentMessage(String message, String errorMessage, boolean saveToFile) throws NotMatchPaymentPatternGetMessage {
        this.saveToFile = saveToFile;
        if (!inputPattern.matcher(message).find()) {
            throw MessagesFactory.newNotMatchPaymentPatternGetMessage(message,errorMessage);
        }
        this.message = message;

    }
    public String getMessage() {
        return message;
    }

    public boolean isSaveToFile() {
        return saveToFile;
    }

    public PaymentMessageMatcher getMatcher() {
        return new PaymentMessageMatcher(this);
    }
}
