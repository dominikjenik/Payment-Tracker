import akka.actor.*;
import akka.event.*;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/24/2015.
 */
public class PaymentTrackerServer {
    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("PaymentTracker", ConfigFactory.load("server.conf"));
        ActorRef router = system.actorOf(Props.create(Router.class), "router");

        Inbox inbox = Inbox.create(system);
        LoggingAdapter logger = Logging.getLogger(system, inbox);
        inbox.send(router, "hello world I want to track my money");
        long time = System.currentTimeMillis();
        Object receive = inbox.receive(Duration.create(5000, TimeUnit.MILLISECONDS));
        logger.info("time: " + (System.currentTimeMillis() - time));
        logger.info("the result is: " + receive);
        system.shutdown();
    }
}