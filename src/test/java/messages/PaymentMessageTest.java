package messages;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static messages.PaymentMessage.inputPattern;


/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessageTest {

    @Test
    public void testPaymentMessageObject() throws NotMatchPaymentPatternMessage {
        new PaymentMessage("USD 1000");
    }

    @Test(expectedExceptions = NotMatchPaymentPatternMessage.class)
    public void testPaymentMessageObjectException() throws NotMatchPaymentPatternMessage {
        new PaymentMessage("U S D 1000");
    }

    @Test
    public void testPatterns() {
        List<String> listForValidation = Lists.newArrayList();
        listForValidation.add("USD 1000");
        listForValidation.add("USD -100");
        listForValidation.add("RMB 2000");
        listForValidation.add("HKD 200");
        listForValidation.add("HKD +200");
        listForValidation.add("RMB 2000 (USD 314.60)");
        listForValidation.add("RMB -2000 (USD 314.60)");
        listForValidation.add("RMB    2000   (USD    314.60)");
        listForValidation.add("HKD 300 (USD 38.62)");
        listForValidation.add("HKD 12345678901234567890123456789012 (USD 38.62)");
        listForValidation.add("HKD 12345678901234567890123456789012.1234 (USD 38.62)");
        for (String item:listForValidation){
            Assert.assertTrue("This should be valid and is not: "+item, inputPattern.matcher(item).find());
        }
        listForValidation.clear();

        listForValidation.add("US 1000");
        listForValidation.add("USD 1000\nUSD 1000");
        listForValidation.add("USD 1000 USD 1000");
        listForValidation.add("USDOLAR 1000");
        listForValidation.add("RMB +2000 (USD +314.60)");
        listForValidation.add("RMB +2000 (USD -314.60)");
        listForValidation.add("RMB -2000 (USD +314.60)");
        listForValidation.add("RMB -2000 (USD -314.60)");
        listForValidation.add("RMB +2000 (US 314.60)");
        listForValidation.add("HKD 123456789012345678901234567890123.0000");
        listForValidation.add("HKD 123456789012345678901234567890123.00005");
        listForValidation.add("HKD 1234567890123456789012345678901234567 (USD 38.62)");
        for (String item:listForValidation){
            Assert.assertFalse("This should be invalid and is not: "+item, inputPattern.matcher(item).find());
        }
    }
}