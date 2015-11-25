import akka.actor.*;
import akka.event.*;
import com.typesafe.config.ConfigFactory;
import scala.Console;
import scala.concurrent.duration.Duration;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/24/2015.
 */
public class PaymentTrackerServer {
    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("PaymentTracker"/*, ConfigFactory.load("server.conf")*/);
        ActorRef router = system.actorOf(Props.create(Router.class), "router");
        Inbox inbox = Inbox.create(system);

        ConsoleLogger consoleLogger=new ConsoleLogger(inbox);
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(consoleLogger);

        Scanner in= new Scanner(System.in);
        while(in.hasNextLine()){
            inbox.send(router, in.nextLine());
        }

        System.out.println("closing...");
        consoleLogger.setRunning(false);
        executorService.shutdown();
        system.shutdown();
    }
}