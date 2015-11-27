package actors;

import com.google.common.collect.Maps;
import messages.PaymentMessage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static actors.TransactionCounter.getPaymentMessages;
import static actors.TransactionCounter.performTransaction;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounterTest {

    private Map<String, BigDecimal> exchangeRateFromXToUsd;
    private Map<String, BigDecimal> currencyToExchangeRate;
    private Map<String, BigDecimal> currencyToAmount;

    @BeforeMethod
    public void setUp() throws Exception {
        currencyToAmount = Maps.newHashMap();
        exchangeRateFromXToUsd = Maps.newHashMap();
        currencyToExchangeRate = Maps.newHashMap();
    }

    @Test
    public void testTransactionUSD() throws Exception {
        performTransaction("USD 200", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("USD"), new BigDecimal(200));
        Assert.assertEquals(currencyToExchangeRate.get("USD"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 0);

        performTransaction("USD 200", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("USD"), new BigDecimal(400));
        Assert.assertEquals(currencyToExchangeRate.get("USD"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 0);
    }

    @Test
    public void testTransactionEUR() throws Exception {
        performTransaction("EUR 100", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(100));
        Assert.assertEquals(currencyToExchangeRate.get("EUR"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 0);

        performTransaction("EUR -100 (USD 250)", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(0));
        Assert.assertEquals(currencyToExchangeRate.get("EUR"), new BigDecimal(2.5));
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 1);

        performTransaction("EUR 0.0001", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal("0.0001"));
        Assert.assertEquals(currencyToExchangeRate.get("EUR"), new BigDecimal(2.5));
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 1);
    }

    @Test
    public void testTransactionBIG() throws Exception {
        String bigNumber = "12345678901234567890123456789012.0001";
        performTransaction("EUR " + bigNumber, currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(bigNumber));
        Assert.assertEquals(currencyToExchangeRate.get("EUR"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 0);

        performTransaction("EUR " + bigNumber, currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(currencyToAmount.get("EUR"), new BigDecimal(bigNumber).multiply(new BigDecimal(2)));
        Assert.assertEquals(currencyToExchangeRate.get("EUR"), null);
        Assert.assertEquals(currencyToAmount.size(), 1);
        Assert.assertEquals(currencyToExchangeRate.size(), 0);

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
}
