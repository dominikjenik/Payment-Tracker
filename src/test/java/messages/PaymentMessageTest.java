package messages;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;

import static messages.PaymentMessage.*;


/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessageTest {

    @Test
    public void testGroupsInMatcher() throws NotMatchPaymentPatternGetMessage {
        Matcher matcher = inputPattern.matcher("RMB 2000 (USD 314.60)");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals(matcher.group(0), "RMB 2000 (USD 314.60)");
        Assert.assertEquals(matcher.group(PAYMENT_CURRENCY_GROUP_IN_PATTERN), "RMB");
        Assert.assertEquals(matcher.group(PAYMENT_AMOUNT_GROUP_IN_PATTERN), "2000");
        Assert.assertEquals(matcher.group(PAYMENT_IN_USD_GROUP_IN_PATTERN), "314.60");

        matcher = inputPattern.matcher("RMB 2000");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals(matcher.group(0), "RMB 2000");
        Assert.assertEquals(matcher.group(PAYMENT_CURRENCY_GROUP_IN_PATTERN), "RMB");
        Assert.assertEquals(matcher.group(PAYMENT_AMOUNT_GROUP_IN_PATTERN), "2000");
        Assert.assertEquals(matcher.group(PAYMENT_IN_USD_GROUP_IN_PATTERN), null);
    }

    @Test
    public void testPaymentMessageObject() throws NotMatchPaymentPatternGetMessage {
        new PaymentMessage("USD 1000");
    }

    @Test(expectedExceptions = NotMatchPaymentPatternGetMessage.class)
    public void testPaymentMessageObjectException() throws NotMatchPaymentPatternGetMessage {
        new PaymentMessage("U S D 1000");
    }

    @Test
    public void testPatterns() {
        for (String item : HelpMessage.validList) {
            Assert.assertTrue(inputPattern.matcher(item).find(), "This should be valid and is not: " + item);
        }
        for (String item : HelpMessage.invalidList) {
            Assert.assertFalse(inputPattern.matcher(item).find(), "This should be invalid and is not: " + item);
        }
    }
}