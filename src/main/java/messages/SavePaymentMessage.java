package messages;

import messages.interfaces.FileHandlerMessageI;

/**
 * Created by Jenik on 11/27/2015.
 */
public class SavePaymentMessage implements FileHandlerMessageI {
    private final PaymentMessage paymentMessage;

    public SavePaymentMessage(PaymentMessage paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public PaymentMessage getPaymentMessage() {
        return paymentMessage;
    }
}
