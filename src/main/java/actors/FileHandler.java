package actors;

import akka.actor.UntypedActor;
import messages.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by dj on 26.11.2015.
 */
public class FileHandler extends UntypedActor {
    private File file;

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ReadFileMessage) {
            file = ((ReadFileMessage) o).getFile();
            try {
                final String fileToString = FileUtils.readFileToString(file);
                for (String line : fileToString.split("\n")) {
                    if (line.trim().length() == 0) {
                        continue;
                    }
                    try {
                        getSender().tell(new PaymentMessage(line,
                                "Could not load from file '" + file.getAbsolutePath() + "'.",
                                false), getSelf());
                    } catch (NotMatchPaymentPatternGetMessage e) {
                        getSender().tell(e, getSelf());
                    }
                }
            } catch (FileNotFoundException e) {
                getContext().parent().tell(new FileNotFoundMessage(file.getAbsolutePath()), getSelf());
            }
        } else if (o instanceof SavePaymentMessage && ((SavePaymentMessage) o).getPaymentMessage().isSaveToFile()) {
            String message = "\n" + ((SavePaymentMessage) o).getPaymentMessage().getMessage();
            FileUtils.writeStringToFile(file, message, true);
        } else {
            unhandled(o);
        }
    }

}
