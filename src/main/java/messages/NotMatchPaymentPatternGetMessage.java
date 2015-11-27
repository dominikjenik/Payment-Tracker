package messages;

import messages.interfaces.ConsoleMessageI;

/**
 * Created by Jenik on 11/27/2015.
 */
public class NotMatchPaymentPatternGetMessage extends Exception implements ConsoleMessageI {
    private final String message;

    public NotMatchPaymentPatternGetMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Message: "+message+" does not match pattern. Use 'help' to get list of example messsages";
    }
}
