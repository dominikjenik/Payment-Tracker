package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import messages.FileNotFoundMessage;
import messages.NotMatchPaymentPatternMessage;
import messages.PaymentMessage;
import messages.ReadFileMessage;
import org.apache.commons.io.FileUtils;
import runner.PaymentTrackerRunner;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by dj on 26.11.2015.
 */
public class FileHandler extends UntypedActor {

    private File file = new File("save.txt");


    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ReadFileMessage) {
            try {
                final String fileToString = FileUtils.readFileToString(file);
                ActorSelection transactionCounter = context().actorSelection(
                        PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS + TransactionCounter.class.getName());
                ActorSelection consoleOutputer = context().actorSelection(
                        PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS + ConsoleOutputer.class.getName());

                for (String line : fileToString.split("\n")) {
                    try{
                        transactionCounter.tell(new PaymentMessage(line),getSelf());
                    } catch (NotMatchPaymentPatternMessage  e){
                        consoleOutputer.tell(e,getSelf());
                    }
                }
            } catch (FileNotFoundException e) {
                getContext().parent().tell(new FileNotFoundMessage(), getSelf());
            }
        } else {
            unhandled(o);
        }
    }

}
