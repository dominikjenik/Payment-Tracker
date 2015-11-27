package actors;

import akka.actor.UntypedActor;
import messages.PaymentMessage;

import java.util.regex.Matcher;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounter extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof PaymentMessage){
            String message = ((PaymentMessage) o).getMessage();
            Matcher matcher = PaymentMessage.inputPattern.matcher(message);
            if (matcher.find()) {
                int group = matcher.groupCount();
                String group1 = matcher.group(0);
            }
            unhandled(o);
        }
        unhandled(o);
    }
}
