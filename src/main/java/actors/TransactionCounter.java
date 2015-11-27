package actors;

import akka.actor.UntypedActor;

/**
 * Created by Jenik on 11/27/2015.
 */
public class TransactionCounter extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        unhandled(o);
    }
}
