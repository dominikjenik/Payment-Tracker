package actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import com.google.common.collect.Maps;
import messages.PaymentMessage;
import runner.PaymentTrackerRunner;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;

import static messages.PaymentMessage.PAYMENT_AMOUNT_GROUP_IN_PATTERN;
import static messages.PaymentMessage.PAYMENT_CURRENCY_GROUP_IN_PATTERN;
import static messages.PaymentMessage.PAYMENT_IN_USD_GROUP_IN_PATTERN;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounter extends UntypedActor {
    private Map<String, BigDecimal> currencyToAmount = Maps.newHashMap();
    private Map<String, BigDecimal> exchangeRateFromXToUsd = Maps.newHashMap();
    private ActorSelection fileHandler;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        fileHandler = context().actorSelection(
                PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS + FileHandler.class.getName());
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof PaymentMessage) {
            performTransaction(((PaymentMessage) o).getMessage(), currencyToAmount, exchangeRateFromXToUsd);
            fileHandler.tell(o, getSelf());
        }
        unhandled(o);
    }

    public static void performTransaction(String message, Map<String, BigDecimal> currencyToAmount,
                                          Map<String, BigDecimal> currencyToExchangeRate) {
        Matcher matcher = PaymentMessage.inputPattern.matcher(message);
        matcher.find();
        String paymentCurrency = matcher.group(PAYMENT_CURRENCY_GROUP_IN_PATTERN);
        BigDecimal paymentAmount = new BigDecimal(matcher.group(PAYMENT_AMOUNT_GROUP_IN_PATTERN));
        if (currencyToAmount.get(paymentCurrency) == null) {
            currencyToAmount.put(paymentCurrency, paymentAmount);
        } else {
            currencyToAmount.put(paymentCurrency, currencyToAmount.get(paymentCurrency).add(paymentAmount));
        }
        String paymentInUsd = matcher.group(PAYMENT_IN_USD_GROUP_IN_PATTERN);
        if (paymentInUsd != null) {
            BigDecimal conversionRate = new BigDecimal(paymentInUsd).divide(paymentAmount.abs());
            currencyToExchangeRate.put(paymentCurrency, conversionRate);
        }
    }
}
