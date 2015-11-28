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
    String PLEASE_WRITE_NEW_FILE_NAME_MESSAGE = "Please write new file name:";
    String WRONG_FORMAT_OF_INPUT_MESSAGE = "Wrong format of input. ";
    String CLOSING_MESSAGE = "closing...";
    String LOAD_FILE_MESSAGE= "Would you like to change default file? y/n";

}
