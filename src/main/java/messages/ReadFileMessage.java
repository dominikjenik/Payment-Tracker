package messages;

import messages.interfaces.FileHandlerMessageI;

import java.io.File;

/**
 * Created by dj on 26.11.2015.
 */
public class ReadFileMessage implements FileHandlerMessageI {
    private final File file;

    ReadFileMessage(String file) {
        this.file = new File(file);
    }

    public File getFile() {
        return file;
    }
}
