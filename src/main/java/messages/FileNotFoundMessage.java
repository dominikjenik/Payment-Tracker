package messages;

import messages.interfaces.ConsoleMessageI;

/**
 * Created by Jenik on 11/26/2015.
 */
public class FileNotFoundMessage implements ConsoleMessageI {
    private final String file;

    FileNotFoundMessage(String file) {
        this.file = file;
    }

    public String getMessage() {
        return "File: "+file+" not found. Nothing to read from.";
    }
}
