package actors;

import akka.actor.UntypedActor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import extraction.PaymentMessageMatcher;
import messages.MessagesFactory;
import messages.NotMatchPaymentPatternGetMessage;
import messages.PaymentMessage;
import messages.TickMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static messages.PaymentMessage.DIGITS_AFTER_DOT;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounter extends UntypedActor {
    private Map<String, BigDecimal> currencyToAmount = Maps.newHashMap();
    private Map<String, BigDecimal> exchangeRateFromXToUsd = Maps.newHashMap();

    public static List<PaymentMessage> getPaymentMessages(Map<String, BigDecimal> currencyToAmount, Map<String, BigDecimal> exchangeRateFromXToUsd) throws NotMatchPaymentPatternGetMessage {
        List<PaymentMessage> paymentMessages = Lists.newArrayList();
        for (Map.Entry<String, BigDecimal> currencyAndAmount : currencyToAmount.entrySet()) {
            String conversion = "";
            if (currencyAndAmount.getValue().equals(BigDecimal.ZERO)) {
                continue;
            }
            if (exchangeRateFromXToUsd.get(currencyAndAmount.getKey()) != null) {
                BigDecimal exchangedToDollar = currencyAndAmount.getValue().multiply(exchangeRateFromXToUsd.get(currencyAndAmount.getKey()))
                        .setScale(DIGITS_AFTER_DOT, RoundingMode.HALF_UP).abs().stripTrailingZeros();
                conversion = " (USD " + exchangedToDollar.toPlainString() + ")";
            }
            paymentMessages.add(MessagesFactory.newPaymentMessage(currencyAndAmount.getKey() + " " + currencyAndAmount.getValue().toPlainString() + conversion));
        }
        return paymentMessages;
    }

    public static void performTransaction(PaymentMessage paymentMessage, Map<String, BigDecimal> currencyToAmount,
                                          Map<String, BigDecimal> currencyToExchangeRate) {
        PaymentMessageMatcher matcher=paymentMessage.getMatcher();
        String paymentCurrency = matcher.getPaymentCurrency();
        BigDecimal paymentAmount = new BigDecimal(matcher.getPaymentAmount());
        if (currencyToAmount.get(paymentCurrency) == null) {
            currencyToAmount.put(paymentCurrency, paymentAmount);
        } else {
            currencyToAmount.put(paymentCurrency, currencyToAmount.get(paymentCurrency).add(paymentAmount));
        }
        String paymentInUsd = matcher.getPaymentInUSD();
        if (paymentInUsd != null) {
            BigDecimal conversionRate = new BigDecimal(paymentInUsd).divide(paymentAmount.abs());
            currencyToExchangeRate.put(paymentCurrency, conversionRate);
        }
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof PaymentMessage) {
            performTransaction(((PaymentMessage) o), currencyToAmount, exchangeRateFromXToUsd);
            getSender().tell(MessagesFactory.newSavePaymentMessage((PaymentMessage) o), getSelf());
        } else if (o instanceof TickMessage) {
            List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, exchangeRateFromXToUsd);
            getSender().tell(MessagesFactory.newPaymentsOverviewMessage(paymentMessages), getSelf());
        } else {
            unhandled(o);
        }
    }
}
