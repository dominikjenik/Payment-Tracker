package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import messages.FileNotFoundMessage;
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
                for (String line : fileToString.split("\n")) {
                    ActorSelection transactionCounter = context().actorSelection(
                            PaymentTrackerRunner.PAYMENT_TRACKER_SYSTEM_ACTORS_ADDRESS + TransactionCounter.class.getName());
                    transactionCounter.tell(line,getSelf());
                }
            } catch (FileNotFoundException e) {
                getContext().parent().tell(new FileNotFoundMessage(), getSelf());
            }
        } else {
            unhandled(o);
        }
    }

}
