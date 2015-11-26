import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.event.Logging;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/25/2015.
 */
public class ConsoleLogger implements Runnable {
    private Inbox inbox;
    private volatile boolean running = true;
    private ActorSystem system;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ConsoleLogger(Inbox inbox, ActorSystem system) {
        this.inbox = inbox;
        this.system = system;
    }

    public void run() {
        while (running) {
            long time = System.currentTimeMillis();
            try {
                Object receive = inbox.receive(Duration.create(5000, TimeUnit.MILLISECONDS));
                System.out.println(receive);
            } catch (TimeoutException e) {
                Logging.getLogger(system, inbox).debug("waiting for response");
            }
        }
    }
}
