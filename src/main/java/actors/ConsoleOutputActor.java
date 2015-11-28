package actors;

import akka.actor.UntypedActor;
import messages.interfaces.ConsoleMessageI;

/**
 * Created by Jenik on 11/25/2015.
 */
class ConsoleOutputActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ConsoleMessageI) {
            System.out.println(((ConsoleMessageI) o).getMessage());
        } else {
            System.out.println(o);
        }
    }

}
