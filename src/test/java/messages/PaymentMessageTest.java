package messages;

import actors.ConsoleOutputer;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;

import static messages.PaymentMessage.*;


/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessageTest {

    @Test
    public void testGroupsInMatcher() throws NotMatchPaymentPatternMessage {
        Matcher matcher = inputPattern.matcher("RMB 2000 (USD 314.60)");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("RMB 2000 (USD 314.60)", matcher.group(0));
        Assert.assertEquals("RMB", matcher.group(PAYMENT_CURRENCY_GROUP_IN_PATTERN));
        Assert.assertEquals("2000", matcher.group(PAYMENT_AMOUNT_GROUP_IN_PATTERN));
        Assert.assertEquals("314.60", matcher.group(PAYMENT_IN_USD_GROUP_IN_PATTERN));

        matcher = inputPattern.matcher("RMB 2000");
        Assert.assertTrue(matcher.find());
        Assert.assertEquals("RMB 2000", matcher.group(0));
        Assert.assertEquals("RMB", matcher.group(PAYMENT_CURRENCY_GROUP_IN_PATTERN));
        Assert.assertEquals("2000", matcher.group(PAYMENT_AMOUNT_GROUP_IN_PATTERN));
        Assert.assertEquals(null, matcher.group(PAYMENT_IN_USD_GROUP_IN_PATTERN));
    }

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
        for (String item : ConsoleOutputer.validList) {
            Assert.assertTrue("This should be valid and is not: " + item, inputPattern.matcher(item).find());
        }
        for (String item : ConsoleOutputer.invalidList) {
            Assert.assertFalse("This should be invalid and is not: " + item, inputPattern.matcher(item).find());
        }
    }
}