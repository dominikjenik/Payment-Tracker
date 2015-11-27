package messages;

import junit.framework.Assert;
import org.testng.annotations.Test;

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
    public void testPatterns(){
        Assert.assertTrue("USD 1000".matches(inputPattern));
        Assert.assertTrue("USD -100".matches(inputPattern));
        Assert.assertTrue("RMB 2000".matches(inputPattern));
        Assert.assertTrue("HKD 200".matches(inputPattern));
        Assert.assertTrue("HKD +200".matches(inputPattern));
        Assert.assertTrue("RMB 2000 (USD 314.60)".matches(inputPattern));
        Assert.assertTrue("RMB -2000 (USD 314.60)".matches(inputPattern));
        Assert.assertTrue("RMB    2000   (USD    314.60)".matches(inputPattern));
        Assert.assertTrue("HKD 300 (USD 38.62)".matches(inputPattern));
        Assert.assertTrue("HKD 12345678901234567890123456789012 (USD 38.62)".matches(inputPattern));
        Assert.assertTrue("HKD 12345678901234567890123456789012.1234 (USD 38.62)".matches(inputPattern));

        Assert.assertFalse("US 1000".matches(inputPattern));
        Assert.assertFalse("USD 1000\nUSD 1000".matches(inputPattern));
        Assert.assertFalse("USD 1000 USD 1000".matches(inputPattern));
        Assert.assertFalse("USDOLAR 1000".matches(inputPattern));
        Assert.assertFalse("RMB +2000 (USD +314.60)".matches(inputPattern));
        Assert.assertFalse("RMB +2000 (USD -314.60)".matches(inputPattern));
        Assert.assertFalse("RMB -2000 (USD +314.60)".matches(inputPattern));
        Assert.assertFalse("RMB -2000 (USD -314.60)".matches(inputPattern));
        Assert.assertFalse("RMB +2000 (US 314.60)".matches(inputPattern));
        Assert.assertFalse("HKD 123456789012345678901234567890123.0000".matches(inputPattern));
        Assert.assertFalse("HKD 123456789012345678901234567890123.00005".matches(inputPattern));
        Assert.assertFalse("HKD 1234567890123456789012345678901234567 (USD 38.62)".matches(inputPattern));
    }
}