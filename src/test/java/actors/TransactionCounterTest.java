package actors;

import com.google.common.collect.Maps;
import junit.framework.Assert;
import messages.PaymentMessage;
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
        Assert.assertEquals(new BigDecimal(200), currencyToAmount.get("USD"));
        Assert.assertEquals(null, currencyToExchangeRate.get("USD"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());

        performTransaction("USD 200", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(400), currencyToAmount.get("USD"));
        Assert.assertEquals(null, currencyToExchangeRate.get("USD"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());
    }

    @Test
    public void testTransactionEUR() throws Exception {
        performTransaction("EUR 100", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(100), currencyToAmount.get("EUR"));
        Assert.assertEquals(null, currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());

        performTransaction("EUR -100 (USD 250)", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(0), currencyToAmount.get("EUR"));
        Assert.assertEquals(new BigDecimal(2.5), currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(1, currencyToExchangeRate.size());

        performTransaction("EUR 0.0001", currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal("0.0001"), currencyToAmount.get("EUR"));
        Assert.assertEquals(new BigDecimal(2.5), currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(1, currencyToExchangeRate.size());
    }

    @Test
    public void testTransactionBIG() throws Exception {
        String bigNumber = "12345678901234567890123456789012.0001";
        performTransaction("EUR " + bigNumber, currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(bigNumber), currencyToAmount.get("EUR"));
        Assert.assertEquals(null, currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());

        performTransaction("EUR " + bigNumber, currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(bigNumber).multiply(new BigDecimal(2)), currencyToAmount.get("EUR"));
        Assert.assertEquals(null, currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());

    }

    @Test
    public void testGetPaymentMessages() throws Exception {
        currencyToAmount.put("USD", new BigDecimal(200));
        List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(1, paymentMessages.size());
        Assert.assertEquals("USD 200", paymentMessages.get(0).getMessage());

        currencyToAmount.put("EUR", new BigDecimal(200));
        paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(2, paymentMessages.size());
        Assert.assertEquals("USD 200", paymentMessages.get(1).getMessage());
        Assert.assertEquals("EUR 200", paymentMessages.get(0).getMessage());

        exchangeRateFromXToUsd.put("EUR", new BigDecimal(2));
        paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(2, paymentMessages.size());
        Assert.assertEquals("USD 200", paymentMessages.get(1).getMessage());
        Assert.assertEquals("EUR 200 (USD 400)", paymentMessages.get(0).getMessage());

        currencyToAmount.put("EUR", new BigDecimal(-600));
        paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
        Assert.assertEquals(2, paymentMessages.size());
        Assert.assertEquals("USD 200", paymentMessages.get(1).getMessage());
        Assert.assertEquals("EUR -600 (USD 1200)", paymentMessages.get(0).getMessage());

    }
}
