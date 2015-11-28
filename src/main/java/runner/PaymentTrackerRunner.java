package runner;

import actors.RouterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import messages.*;

import java.util.Scanner;

import static logging.OutputMessages.*;

/**
 * Created by Jenik on 11/24/2015.
 */
public class PaymentTrackerRunner {

    public static final String TERMINATE_MESSAGE = "quit";
    public static final String HELP_MESSAGE = "help";
    public static final String PRINT_MESSAGE = "print";
    public static final String PAYMENT_TRACKER_SYSTEM = "PaymentTracker";

    public static void main(String[] args)  {
        System.out.println(LOADING_MESSAGE);
        ActorSystem system = ActorSystem.create(PAYMENT_TRACKER_SYSTEM, ConfigFactory.defaultApplication());
        ActorRef router = system.actorOf(Props.create(RouterActor.class), RouterActor.class.getName());
        Inbox inbox = Inbox.create(system);

        System.out.println(WELCOME_MESSAGE);
        loadFromFile(args, router, inbox);
        readInputFromUser(router, inbox);

        System.out.println(CLOSING_MESSAGE);
        system.terminate();
    }

    public static void loadFromFile(String[] args, ActorRef router, Inbox inbox) {
        if (args.length!=0){
            inbox.send(router, MessagesFactory.newReadFileMessage(args[0]));;
        }else {
            inbox.send(router, MessagesFactory.newReadFileMessage("save.txt"));
        }
    }

    public static void readInputFromUser(ActorRef router, Inbox inbox) {
        Scanner in = new Scanner(System.in);
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
                inbox.send(router, MessagesFactory.newPaymentMessage(line, WRONG_FORMAT_OF_INPUT_MESSAGE));
            } catch (NotMatchPaymentPatternGetMessage e) {
                inbox.send(router, e);
            }
        }
    }

}