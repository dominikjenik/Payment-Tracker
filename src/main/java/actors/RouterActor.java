package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import messages.MessagesFactory;
import messages.PaymentMessage;
import messages.TickMessage;
import messages.interfaces.ConsoleMessageI;
import messages.interfaces.FileHandlerMessageI;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jenik on 11/24/2015.
 */
public class RouterActor extends UntypedActor {
    private static final FiniteDuration LIST_PAYMENTS_TO_OUTPUT_PERIOD_IN_SECONDS = Duration.create(50, TimeUnit.SECONDS);
    private final ActorRef fileHandler = getContext().actorOf(Props.create(FileHandlerActor.class), FileHandlerActor.class.getName());
    private final ActorRef consoleOutputActor = getContext().actorOf(Props.create(ConsoleOutputActor.class), ConsoleOutputActor.class.getName());
    private final ActorRef transactionCounter = getContext().actorOf(Props.create(TransactionCounterActor.class), TransactionCounterActor.class.getName());

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getContext().system().scheduler().schedule(LIST_PAYMENTS_TO_OUTPUT_PERIOD_IN_SECONDS,
                LIST_PAYMENTS_TO_OUTPUT_PERIOD_IN_SECONDS,
                getSelf(), MessagesFactory.newTickMessage(), getContext().system().dispatcher(), null);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ConsoleMessageI) {
            consoleOutputActor.tell(o, getSelf());
        } else if (o instanceof PaymentMessage || o instanceof TickMessage) {
            transactionCounter.tell(o, getSelf());
        } else if (o instanceof FileHandlerMessageI) {
            fileHandler.tell(o, getSelf());
        } else {
            unhandled(o);
        }
    }
}
