package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import messages.FileNotFoundMessage;
import messages.NotMatchPaymentPatternMessage;
import messages.PaymentMessage;
import messages.ReadFileMessage;

/**
 * Created by Jenik on 11/24/2015.
 */
public class Router extends UntypedActor {
    private ActorRef fileHandler = getContext().actorOf(Props.create(FileHandler.class), FileHandler.class.getName());
    private ActorRef consoleOutputer = getContext().actorOf(Props.create(ConsoleOutputer.class), ConsoleOutputer.class.getName());
    private ActorRef transactionCounter = getContext().actorOf(Props.create(TransactionCounter.class), TransactionCounter.class.getName());

    @Override
    public void preStart() throws Exception {
        super.preStart();
        fileHandler.tell(new ReadFileMessage(), getSelf());
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof FileNotFoundMessage || o instanceof NotMatchPaymentPatternMessage) {
            consoleOutputer.tell(o, getSelf());
        } else if (o instanceof PaymentMessage) {
            transactionCounter.tell(o, getSelf());
        } else {
            consoleOutputer.tell("Yeah sure sir", getSelf());
        }
    }
}
