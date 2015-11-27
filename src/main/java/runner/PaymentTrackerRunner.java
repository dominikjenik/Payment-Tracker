package runner;

import actors.Router;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import messages.HelpMessage;
import messages.NotMatchPaymentPatternMessage;
import messages.PaymentMessage;
import messages.TickMessage;

import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/24/2015.
 */
public class PaymentTrackerRunner {

    public static final String TERMINATE_MESSAGE = "quit";
    public static final String HELP_MESSAGE = "help";
    public static final String PRINT_MESSAGE = "print";
    public static final String PAYMENT_TRACKER_SYSTEM = "PaymentTracker";
    public static final String PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS
            = "akka://" + PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM + "/user/";

    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create(PAYMENT_TRACKER_SYSTEM, ConfigFactory.load("debug.conf"));
        ActorRef router = system.actorOf(Props.create(Router.class), Router.class.getName());

        Inbox inbox = Inbox.create(system);
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            final String line = in.nextLine();
            if (TERMINATE_MESSAGE.equals(line)) {
                break;
            }
            if (HELP_MESSAGE.equals(line)) {
                inbox.send(router, new HelpMessage());
                continue;
            }
            if (PRINT_MESSAGE.equals(line)) {
                inbox.send(router, new TickMessage());
                continue;
            }
            try {
                inbox.send(router, new PaymentMessage(line));
            } catch (NotMatchPaymentPatternMessage e) {
                inbox.send(router, e);
            }
        }
        System.out.println("closing...");
        system.terminate();
    }
}