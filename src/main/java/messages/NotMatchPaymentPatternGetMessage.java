package messages;

import messages.interfaces.ConsoleMessageI;

/**
 * Created by Jenik on 11/27/2015.
 */
public class NotMatchPaymentPatternGetMessage extends Exception implements ConsoleMessageI {
    private final String message;
    private String errorMessage;

    public NotMatchPaymentPatternGetMessage(String message, String errorMessage) {
        this.message = message;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage+" Message: "+message+" does not match pattern. Use 'help' to get list of example messages.";
    }
}
