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
import static messages.PaymentMessage.DIGITS_BEFORE_DOT;

/**
 * Created by Jenik on 11/27/2015.
 */
class TransactionCounterActor extends UntypedActor {
    private final Map<String, BigDecimal> currencyToAmount = Maps.newHashMap();
    private final Map<String, BigDecimal> currencyToExchangeRate = Maps.newHashMap();

    public static List<PaymentMessage> getPaymentMessages(Map<String, BigDecimal> currencyToAmount, Map<String, BigDecimal> currencyToExchangeRate) throws NotMatchPaymentPatternGetMessage {
        List<PaymentMessage> paymentMessages = Lists.newArrayList();
        for (Map.Entry<String, BigDecimal> currencyAndAmount : currencyToAmount.entrySet()) {
            String conversion = "";
            if (currencyAndAmount.getValue().equals(BigDecimal.ZERO)) {
                continue;
            }
            if (currencyToExchangeRate.get(currencyAndAmount.getKey()) != null) {
                BigDecimal exchangedToDollar = currencyAndAmount.getValue().multiply(currencyToExchangeRate.get(currencyAndAmount.getKey()))
                        .setScale(DIGITS_AFTER_DOT, RoundingMode.HALF_UP).abs().stripTrailingZeros();
                if (!exchangedToDollar.equals(BigDecimal.ZERO)){
                    conversion = " (USD " + exchangedToDollar.toPlainString() + ")";
                }
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
            currencyToAmount.put(paymentCurrency, paymentAmount.stripTrailingZeros());
        } else {
            currencyToAmount.put(paymentCurrency, currencyToAmount.get(paymentCurrency).add(paymentAmount).stripTrailingZeros());
        }
        String paymentInUsd = matcher.getPaymentInUSD();
        if (paymentInUsd != null) {
            BigDecimal conversionRate = new BigDecimal(paymentInUsd).divide(paymentAmount.abs(),DIGITS_BEFORE_DOT+DIGITS_AFTER_DOT,BigDecimal.ROUND_HALF_UP);
            currencyToExchangeRate.put(paymentCurrency, conversionRate .stripTrailingZeros());
        }
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof PaymentMessage) {
            performTransaction(((PaymentMessage) o), currencyToAmount, currencyToExchangeRate);
            getSender().tell(MessagesFactory.newSavePaymentMessage((PaymentMessage) o), getSelf());
        } else if (o instanceof TickMessage) {
            List<PaymentMessage> paymentMessages = getPaymentMessages(currencyToAmount, currencyToExchangeRate);
            getSender().tell(MessagesFactory.newPaymentsOverviewMessage(paymentMessages), getSelf());
        } else {
            unhandled(o);
        }
    }
}
