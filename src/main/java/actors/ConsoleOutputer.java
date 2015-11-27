package actors;

import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.UntypedActor;
import akka.event.Logging;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/25/2015.
 */
public class ConsoleOutputer extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        System.out.println(o);
    }

}
