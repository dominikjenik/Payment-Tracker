package actors;

import akka.actor.UntypedActor;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by dj on 26.11.2015.
 */
public class FileHandler extends UntypedActor {

    private File file = new File("save.txt");

    @Override
    public void preStart() throws Exception {
        super.preStart();
        final String s = FileUtils.readFileToString(file);
    }

    @Override
    public void onReceive(Object o) throws Exception {

    }

}
