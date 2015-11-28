package messages;

import java.util.List;

/**
 * Created by Jenik on 11/28/2015.
 */
public class MessagesFactory {
    public static PaymentMessage newPaymentMessage(String message, String errorMessage, boolean saveToFile) throws NotMatchPaymentPatternGetMessage {
        return new PaymentMessage(message, errorMessage, saveToFile);
    }

    public static PaymentMessage newPaymentMessage(String message, String errorMessage) throws NotMatchPaymentPatternGetMessage {
        return new PaymentMessage(message, errorMessage, true);
    }

    public static PaymentMessage newPaymentMessage(String message) throws NotMatchPaymentPatternGetMessage {
        return new PaymentMessage(message, null, true);
    }

    public static TickMessage newTickMessage() {
        return new TickMessage();
    }

    public static HelpMessage newHelpMessage() {
        return new HelpMessage();
    }

    public static SavePaymentMessage newSavePaymentMessage(PaymentMessage paymentMessage) {
        return new SavePaymentMessage(paymentMessage);
    }
    public static NotMatchPaymentPatternGetMessage newNotMatchPaymentPatternGetMessage(String message, String errorMessage) {
        return new NotMatchPaymentPatternGetMessage(message, errorMessage);
    }

    public static FileNotFoundMessage newFileNotFoundMessage(String absolutePath) {
        return new FileNotFoundMessage(absolutePath);
    }

    public static ReadFileMessage newReadFileMessage(String file) {
        return new ReadFileMessage(file);
    }

    public static PaymentsOverviewMessage newPaymentsOverviewMessage(List<PaymentMessage> paymentMessages) {
        return new PaymentsOverviewMessage(paymentMessages);
    }
}
