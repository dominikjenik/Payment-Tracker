package messages;

/**
 * Created by Jenik on 11/27/2015.
 */
public class SavePaymentMessage {
    private final PaymentMessage paymentMessage;

    public SavePaymentMessage(PaymentMessage paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public PaymentMessage getPaymentMessage() {
        return paymentMessage;
    }
}
