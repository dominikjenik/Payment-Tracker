package actors;

import com.google.common.collect.Maps;
import messages.MessagesFactory;
import messages.NotMatchPaymentPatternGetMessage;
import messages.PaymentMessage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static actors.TransactionCounterActor.getPaymentMessages;
import static actors.TransactionCounterActor.isUnderTheMaxValue;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounterActorTest {

    private Map<String, BigDecimal> exchangeRateFromXToUsd;
    private Map<String, BigDecimal> currencyToAmount;

    private static void performTransaction(String message, Map<String, BigDecimal> currencyToAmount, Map<String, BigDecimal> currencyToExchangeAmount) throws NotMatchPaymentPatternGetMessage {
        TransactionCounterActor.performTransaction(MessagesFactory.newPaymentMessage(message), currencyToAmount, currencyToExchangeAmount);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        currencyToAmount = Maps.newHashMap();
        exchangeRateFromXToUsd = Maps.newHashMap();
    }

    @Test
    public void twoTimesAddTheSame() throws Exception {
        performTransaction("USD 200", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("USD").toPlainString(), "200");
        Assert.assertEquals(exchangeRateFromXToUsd.get("USD"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 0);

        performTransaction("USD 200", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("USD").toPlainString(), "400");
        Assert.assertEquals(exchangeRateFromXToUsd.get("USD"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 0);
    }

    @Test
    public void addingLowestPossibleValue() throws Exception {
        performTransaction("EUR 100", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR").toPlainString(), "100");
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 0);

        performTransaction("EUR -100 (USD 250)", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(0));
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR"), new BigDecimal(2.5));
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 1);

        performTransaction("EUR 0.0001", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal("0.0001"));
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR"), new BigDecimal(2.5));
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 1);
    }

    @Test
    public void twoTimesAddingBigNumber() throws Exception {
        String bigNumber = "12345678901234567890123456789012.0001";
        performTransaction("EUR " + bigNumber, currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(bigNumber));
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 0);

        performTransaction("EUR " + bigNumber, currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(bigNumber).multiply(new BigDecimal(2)));
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 0);

    }

    @Test
    public void testGetPaymentMessages() throws Exception {
        currencyToAmount.put("USD", new BigDecimal(200));
        List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(), 1);
        Assert.assertEquals(paymentMessages.get(0).getMessage(), "USD 200");

        currencyToAmount.put("EUR", new BigDecimal(200));
        paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(), 2);
        Assert.assertEquals(paymentMessages.get(1).getMessage(), "USD 200");
        Assert.assertEquals(paymentMessages.get(0).getMessage(), "EUR 200");

        exchangeRateFromXToUsd.put("EUR", new BigDecimal(2));
        paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(), 2);
        Assert.assertEquals(paymentMessages.get(1).getMessage(), "USD 200");
        Assert.assertEquals(paymentMessages.get(0).getMessage(), "EUR 200 (USD 400)");

        currencyToAmount.put("EUR", new BigDecimal(-600));
        paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(), 2);
        Assert.assertEquals(paymentMessages.get(1).getMessage(), "USD 200");
        Assert.assertEquals(paymentMessages.get(0).getMessage(), "EUR -600 (USD 1200)");
    }

    @Test
    public void testGetPaymentMessagesEmpty() throws Exception {
        currencyToAmount.put("USD", new BigDecimal(0));
        List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(), 0);
    }

    @Test
    public void exchangeWithSmallRate() throws Exception {
        currencyToAmount.put("EUR", new BigDecimal(1));
        exchangeRateFromXToUsd.put("EUR", new BigDecimal("0.00001"));
        List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(), 1);
        Assert.assertEquals(paymentMessages.get(0).getMessage(), "EUR 1");
    }

    @Test
    public void testTransactionEURtoUSDwhereUSDisLowerThanEUR() throws Exception {
        performTransaction("EUR 300 (USD 100)", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR").toPlainString(), "300");
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR"), new BigDecimal(100).divide(new BigDecimal(300), PaymentMessage.DIGITS_BEFORE_DOT+PaymentMessage.DIGITS_AFTER_DOT, RoundingMode.HALF_UP));
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(exchangeRateFromXToUsd.size(), 1);
    }

    @Test
    public void testTrailingZeros() throws Exception {
        performTransaction("EUR 0.01", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal("0.01"));
        performTransaction("EUR -1.01", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal("-1"));

        performTransaction("USD 0.0100", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("USD"), new BigDecimal("0.01"));
    }

    @Test
    public void addingCurrencyValueCloseToZero() throws Exception {
        performTransaction("EUR 200 (USD 0.0001)", currencyToAmount, exchangeRateFromXToUsd);
        performTransaction("EUR -100", currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(currencyToAmount.get("EUR").toPlainString(), "100");
        Assert.assertEquals(exchangeRateFromXToUsd.get("EUR").toPlainString(), "0.0000005");
    }

    @Test
    public void addMaxValueToExchangeAmountAndIncreaseAmount() throws NotMatchPaymentPatternGetMessage {
        performTransaction("XXX 0.0001 (USD 12345678901234567890123456789012)", currencyToAmount, exchangeRateFromXToUsd);
        performTransaction("XXX 10", currencyToAmount, exchangeRateFromXToUsd);
        List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(),1);
    }

    @Test
    public void isUnderTheMaxValueFunctionTest(){
        Assert.assertFalse(isUnderTheMaxValue(new BigDecimal("1e"+(PaymentMessage.DIGITS_BEFORE_DOT))));
        Assert.assertTrue(isUnderTheMaxValue(new BigDecimal("1e"+(PaymentMessage.DIGITS_BEFORE_DOT)).subtract(BigDecimal.ONE)));
    }

    @Test
    public void addBigValuesOverLimit() throws NotMatchPaymentPatternGetMessage {
        performTransaction("XXX 52345678901234567890123456789012", currencyToAmount, exchangeRateFromXToUsd);
        performTransaction("XXX 52345678901234567890123456789012", currencyToAmount, exchangeRateFromXToUsd);
        List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(paymentMessages.size(),0);
    }
}
