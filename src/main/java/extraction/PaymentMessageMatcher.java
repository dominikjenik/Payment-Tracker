package extraction;

import messages.PaymentMessage;

import java.util.regex.Matcher;

/**
 * Created by Jenik on 11/28/2015.
 */
public class PaymentMessageMatcher {
    private final Matcher matcher;
    private static final int PAYMENT_CURRENCY_GROUP_IN_PATTERN = 1;
    private static final int PAYMENT_AMOUNT_GROUP_IN_PATTERN = 2;
    private static final int PAYMENT_IN_USD_GROUP_IN_PATTERN = 6;

    public PaymentMessageMatcher(PaymentMessage paymentMessage) {
        matcher = PaymentMessage.inputPattern.matcher(paymentMessage.getMessage());
        matcher.find();
    }

    public String getPaymentCurrency() {
        return matcher.group(PAYMENT_CURRENCY_GROUP_IN_PATTERN);
    }

    public String getPaymentAmount() {
        return matcher.group(PAYMENT_AMOUNT_GROUP_IN_PATTERN);
    }

    public String getPaymentInUSD() {
        return matcher.group(PAYMENT_IN_USD_GROUP_IN_PATTERN);
    }

    public String getFullMessage() {
        return matcher.group(0);
    }
}
