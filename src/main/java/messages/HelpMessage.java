package messages;

import com.google.common.collect.Lists;
import messages.interfaces.ConsoleMessageI;
import runner.PaymentTrackerRunner;

import java.util.List;

/**
 * Created by Jenik on 11/27/2015.
 */
public class HelpMessage implements ConsoleMessageI {
    public final static List<String> validList = Lists.newArrayList();
    public final static List<String> invalidList = Lists.newArrayList();

    static {
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
        invalidList.add("DOLLAR 1000");
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

    HelpMessage() {
        super();
    }

    public String getMessage() {
        return "List of examples of valid messages:" +
                validList +
                "\n" +
                "List of examples of not allowed messages:" +
                invalidList +
                "\n" +
                "List of invalid commands: " +
                PaymentTrackerRunner.HELP_MESSAGE +
                ", " +
                PaymentTrackerRunner.PRINT_MESSAGE +
                ", " +
                PaymentTrackerRunner.TERMINATE_MESSAGE;
    }
}
