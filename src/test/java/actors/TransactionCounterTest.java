package actors;

import com.google.common.collect.Maps;
import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

import static actors.TransactionCounter.performTransaction;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounterTest {

    private Map<String, BigDecimal> currencyToExchangeRate;
    private Map<String, BigDecimal> currencyToAmount;

    @BeforeMethod
    public void setUp() throws Exception {
        currencyToAmount = Maps.newHashMap();
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
        String bigNumber="12345678901234567890123456789012.0001";
        performTransaction("EUR "+bigNumber, currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(bigNumber), currencyToAmount.get("EUR"));
        Assert.assertEquals(null, currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());

        performTransaction("EUR "+bigNumber, currencyToAmount, currencyToExchangeRate);
        Assert.assertEquals(new BigDecimal(bigNumber).multiply(new BigDecimal(2)), currencyToAmount.get("EUR"));
        Assert.assertEquals(null, currencyToExchangeRate.get("EUR"));
        Assert.assertEquals(1, currencyToAmount.size());
        Assert.assertEquals(0, currencyToExchangeRate.size());

    }
}