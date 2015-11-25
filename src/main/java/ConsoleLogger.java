import akka.actor.Inbox;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/25/2015.
 */
public class ConsoleLogger implements Runnable {
    private Inbox inbox;
    private volatile boolean running=true;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ConsoleLogger(Inbox inbox) {
        this.inbox = inbox;
    }

    public void run() {
        while (running) {
            long time = System.currentTimeMillis();
            try {
                Object receive = inbox.receive(Duration.create(5000, TimeUnit.MILLISECONDS));
                System.out.println(receive);
            } catch (TimeoutException e) {
//                e.printStackTrace();
                System.out.println("waiting for response");
            }
        }
//        System.out.println("closing logger");
    }
}
