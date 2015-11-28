package messages;

import extraction.PaymentMessageMatcher;
import org.testng.Assert;
import org.testng.annotations.Test;

import static messages.PaymentMessage.*;


/**
 * Created by Jenik on 11/27/2015.
 */
public class PaymentMessageMatcherTest {

    @Test
    public void testGroupsInMatcher() throws NotMatchPaymentPatternGetMessage {
        PaymentMessageMatcher matcher = new PaymentMessageMatcher(MessagesFactory.newPaymentMessage("RMB 2000 (USD 314.60)"));
        Assert.assertEquals(matcher.getFullMessage(), "RMB 2000 (USD 314.60)");
        Assert.assertEquals(matcher.getPaymentCurrency(), "RMB");
        Assert.assertEquals(matcher.getPaymentAmount(), "2000");
        Assert.assertEquals(matcher.getPaymentInUSD(), "314.60");

        matcher = MessagesFactory.newPaymentMessage("RMB 2000").getMatcher();
        Assert.assertEquals(matcher.getFullMessage(), "RMB 2000");
        Assert.assertEquals(matcher.getPaymentCurrency(), "RMB");
        Assert.assertEquals(matcher.getPaymentAmount(), "2000");
        Assert.assertEquals(matcher.getPaymentInUSD(), null);
    }

    @Test
    public void testPaymentMessageObject() throws NotMatchPaymentPatternGetMessage {
        MessagesFactory.newPaymentMessage("USD 1000");
    }

    @Test(expectedExceptions = NotMatchPaymentPatternGetMessage.class)
    public void testPaymentMessageObjectException() throws NotMatchPaymentPatternGetMessage {
        MessagesFactory.newPaymentMessage("U S D 1000");
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