package messages;

import com.google.common.collect.ImmutableList;
import messages.interfaces.ConsoleMessageI;

import java.util.List;

/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentsOverviewMessage implements ConsoleMessageI {
    private final ImmutableList<PaymentMessage> paymentMessages;

    PaymentsOverviewMessage(List<PaymentMessage> list) {
        this.paymentMessages = ImmutableList.copyOf(list);
    }

    public String getMessage() {
        StringBuilder sb=new StringBuilder();
        sb.append("\nPayments:\n");
        sb.append("---------\n");
        for (PaymentMessage paymentMessage: paymentMessages){
            sb.append(paymentMessage.getMessage()).append("\n");
        }
        sb.append("---------\n");
        return sb.toString();
    }
}
