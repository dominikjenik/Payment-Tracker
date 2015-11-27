package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import messages.*;
import messages.interfaces.ConsoleMessageI;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jenik on 11/24/2015.
 */
public class Router extends UntypedActor {
    public static final FiniteDuration LIST_PAYMENTS_TO_OUTPUT_PERIOD_IN_SECONDS = Duration.create(50, TimeUnit.SECONDS);
    private ActorRef fileHandler;
    private ActorRef consoleOutputer;
    private ActorRef transactionCounter;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        fileHandler = getContext().actorOf(Props.create(FileHandler.class), FileHandler.class.getName());
        consoleOutputer = getContext().actorOf(Props.create(ConsoleOutputer.class), ConsoleOutputer.class.getName());
        transactionCounter = getContext().actorOf(Props.create(TransactionCounter.class), TransactionCounter.class.getName());
        fileHandler.tell(new ReadFileMessage(), getSelf());
        getContext().system().scheduler().schedule(Duration.Zero(), LIST_PAYMENTS_TO_OUTPUT_PERIOD_IN_SECONDS,
                getSelf(), new TickMessage(), getContext().system().dispatcher(), null);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ConsoleMessageI) {
            consoleOutputer.tell(o, getSelf());
        } else if (o instanceof PaymentMessage || o instanceof TickMessage) {
            transactionCounter.tell(o, getSelf());
        } else if (o instanceof SavePaymentMessage) {
            fileHandler.tell(o, getSelf());
        } else {
            unhandled(o);
        }
    }
}
