package actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import messages.NotMatchPaymentPatternMessage;
import messages.PaymentMessage;
import messages.PaymentsOverviewMessage;
import messages.TickMessage;
import runner.PaymentTrackerRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static messages.PaymentMessage.*;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounter extends UntypedActor {
    private Map<String, BigDecimal> currencyToAmount = Maps.newHashMap();
    private Map<String, BigDecimal> exchangeRateFromXToUsd = Maps.newHashMap();
    private ActorSelection fileHandler;
    private ActorSelection consoleOutputer;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        fileHandler = context().actorSelection(
                PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS + FileHandler.class.getName());
        consoleOutputer = context().actorSelection(
                PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS + ConsoleOutputer.class.getName());
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof PaymentMessage) {
            performTransaction(((PaymentMessage) o).getMessage(), currencyToAmount, exchangeRateFromXToUsd);
            fileHandler.tell(o, getSelf());
        } else if (o instanceof TickMessage) {
            List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
            consoleOutputer.tell(new PaymentsOverviewMessage(paymentMessages), getSelf());
        }
        unhandled(o);
    }

    public static List<PaymentMessage> getPaymentMessages(Map<String, BigDecimal> currencyToAmount, Map<String, BigDecimal> exchangeRateFromXToUsd) throws NotMatchPaymentPatternMessage {
        List<PaymentMessage> paymentMessages = Lists.newArrayList();
        for (Map.Entry<String, BigDecimal> currencyAndAmount : currencyToAmount.entrySet()) {
            String conversion = "";
            if (exchangeRateFromXToUsd.get(currencyAndAmount.getKey())!= null) {
                BigDecimal exchangedToDollar=currencyAndAmount.getValue().multiply(exchangeRateFromXToUsd.get(currencyAndAmount.getKey()))
                        .setScale(DIGITS_AFTER_DOT, RoundingMode.HALF_UP).abs();
                conversion = "(USD "+exchangedToDollar.toPlainString()+")";
            }
            paymentMessages.add(new PaymentMessage(currencyAndAmount.getKey() + " " + currencyAndAmount.getValue().toPlainString() + conversion));
        }
        return paymentMessages;
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
