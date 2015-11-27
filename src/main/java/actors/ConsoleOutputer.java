package actors;

import akka.actor.UntypedActor;
import com.google.common.collect.Lists;
import messages.HelpMessage;

import java.util.List;

/**
 * Created by Jenik on 11/25/2015.
 */
public class ConsoleOutputer extends UntypedActor {
    public final static List<String> validList = Lists.newArrayList();
    public final static List<String> invalidList = Lists.newArrayList();

    {
        validList.add("USD 1000");
        validList.add("USD -100");
        validList.add("RMB 2000");
        validList.add("HKD 200");
        validList.add("HKD +200");
        validList.add("RMB 2000 (USD 314.60)");
        validList.add("RMB -2000 (USD 314.60)");
        validList.add("RMB    2000   (USD    314.60)");
        validList.add("HKD 300 (USD 38.62)");
        validList.add("HKD 12345678901234567890123456789012 (USD 38.62)");
        validList.add("HKD 12345678901234567890123456789012.1234 (USD 38.62)");

        invalidList.add("US 1000");
        invalidList.add("USD 0");
        invalidList.add("USD 10 (USD 10)");
        invalidList.add("USD 0.0");
        invalidList.add("USD 1000\nEUR 1000");
        invalidList.add("USD 1000 USD 1000");
        invalidList.add("USDOLAR 1000");
        invalidList.add("RMB +2000 (USD +314.60)");
        invalidList.add("RMB +2000 (USD -314.60)");
        invalidList.add("RMB -2000 (USD +314.60)");
        invalidList.add("RMB -2000 (USD -314.60)");
        invalidList.add("RMB +2000 (US 314.60)");
        invalidList.add("RMB 10 (US 0)");
        invalidList.add("RMB 10 (US 0.0)");
        invalidList.add("RMB 0.0 (US 0.1)");
        invalidList.add("HKD 123456789012345678901234567890123.0000");
        invalidList.add("HKD 123456789012345678901234567890123.00005");
        invalidList.add("HKD 1234567890123456789012345678901234567 (USD 38.62)");
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof HelpMessage) {
            System.out.println("List of examples of allowed messages:"+validList);
            System.out.println("List of examples of not allowed messages:"+invalidList);

        } else {
            System.out.println(o);
        }
    }

}
