package messages;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentsOverviewMessage {
    private final ImmutableList<PaymentMessage> paymentMessages;

    public PaymentsOverviewMessage(List<PaymentMessage> list) {
        this.paymentMessages = ImmutableList.copyOf(list);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("\nPayments:\n");
        sb.append("---------\n");
        for (PaymentMessage paymentMessage: paymentMessages){
            sb.append(paymentMessage).append("\n");
        }
        sb.append("---------\n");
        return sb.toString();
    }
}
