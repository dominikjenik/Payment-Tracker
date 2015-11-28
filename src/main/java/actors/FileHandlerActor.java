package actors;

import akka.actor.UntypedActor;
import logging.OutputMessages;
import messages.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by dj on 26.11.2015.
 */
class FileHandlerActor extends UntypedActor {
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
                        getSender().tell(MessagesFactory.newPaymentMessageDoNotSaveToFile(line,
                                OutputMessages.WRONG_FORMAT_OF_INPUT_MESSAGE +"Could not load from file '" + file.getAbsolutePath() + "'."), getSelf());
                    } catch (NotMatchPaymentPatternGetMessage e) {
                        getSender().tell(e, getSelf());
                    }
                }
            } catch (FileNotFoundException e) {
                getContext().parent().tell(MessagesFactory.newFileNotFoundMessage(file.getAbsolutePath()), getSelf());
            }
        } else if (o instanceof SavePaymentMessage && ((SavePaymentMessage) o).getPaymentMessage().isSaveToFile()) {
            String message = "\n" + ((SavePaymentMessage) o).getPaymentMessage().getMessage();
            FileUtils.writeStringToFile(file, message, true);
        } else {
            unhandled(o);
        }
    }

}
