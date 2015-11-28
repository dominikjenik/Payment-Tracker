package logging;

import runner.PaymentTrackerRunner;

/**
 * Created by Jenik on 11/28/2015.
 */
public interface OutputMessages {
    String WELCOME_MESSAGE = "Welcome to " + PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM + "\n" +
            "=========================\n" +
            "anytime use 'help' to get instructions";
    String LOADING_MESSAGE = "loading...";
    String WRONG_FORMAT_OF_INPUT_MESSAGE = "Wrong format of input. ";
    String CLOSING_MESSAGE = "closing...";

}
