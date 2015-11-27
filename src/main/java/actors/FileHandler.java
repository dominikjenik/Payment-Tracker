package actors;

import akka.actor.UntypedActor;
import messages.FileNotFoundMessage;
import messages.NotMatchPaymentPatternGetMessage;
import messages.PaymentMessage;
import messages.ReadFileMessage;
import org.apache.commons.io.FileUtils;

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
                    try {
                        getSender().tell(new PaymentMessage(line), getSelf());
                    } catch (NotMatchPaymentPatternGetMessage e) {
                        getSender().tell(e, getSelf());
                    }
                }
            } catch (FileNotFoundException e) {
                getContext().parent().tell(new FileNotFoundMessage(file.getAbsolutePath()), getSelf());
            }
        } else if (o instanceof PaymentMessage) {
            String message = ((PaymentMessage) o).getMessage();
            FileUtils.writeStringToFile(file, message, true);
        } else {
            unhandled(o);
        }
    }

}
