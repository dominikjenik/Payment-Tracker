package runner;

import actors.Router;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import messages.*;

import java.util.Scanner;

/**
 * Created by Jenik on 11/24/2015.
 */
public class PaymentTrackerRunner {

    public static final String TERMINATE_MESSAGE = "quit";
    public static final String HELP_MESSAGE = "help";
    public static final String PRINT_MESSAGE = "print";
    public static final String PAYMENT_TRACKER_SYSTEM = "PaymentTracker";

    public static void main(String[] args)  {
        System.out.println("loading...");
        ActorSystem system = ActorSystem.create(PAYMENT_TRACKER_SYSTEM, ConfigFactory.defaultApplication());
        ActorRef router = system.actorOf(Props.create(Router.class), Router.class.getName());
        Inbox inbox = Inbox.create(system);
        System.out.println("Welcome to "+PAYMENT_TRACKER_SYSTEM);
        System.out.println("=========================");
        System.out.println("anytime use 'help' to get instructions");
        System.out.println("Would you like to change default file? y/n");
        Scanner in = new Scanner(System.in);
        if ("y".equals(in.nextLine())) {
            System.out.println("Please write new file name:");
            String input = in.nextLine();
            inbox.send(router, MessagesFactory.newReadFileMessage(input));
        } else {
            inbox.send(router, MessagesFactory.newReadFileMessage("save.txt"));
        }
        while (in.hasNextLine()) {
            final String line = in.nextLine();
            if (TERMINATE_MESSAGE.equals(line)) {
                break;
            }
            if (HELP_MESSAGE.equals(line)) {
                inbox.send(router, MessagesFactory.newHelpMessage());
                continue;
            }
            if (PRINT_MESSAGE.equals(line)) {
                inbox.send(router, MessagesFactory.newTickMessage());
                continue;
            }
            try {
                inbox.send(router, MessagesFactory.newPaymentMessage(line, "Wrong format of input from console. "));
            } catch (NotMatchPaymentPatternGetMessage e) {
                inbox.send(router, e);
            }
        }
        System.out.println("closing...");
        system.terminate();
    }
}